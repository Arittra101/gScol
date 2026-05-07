package org.getscol.gscol.feature.profile.data.download

import io.ktor.client.HttpClient
import io.ktor.client.request.prepareGet
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.data.api_service.DocumentDownloadApiService
import org.getscol.gscol.feature.profile.domain.download.DocumentDownloadManager
import org.getscol.gscol.feature.profile.domain.download.DownloadState

class DocumentDownloadManagerImpl(
    private val apiService: DocumentDownloadApiService,
    private val httpClient: HttpClient,
    private val writer: PublicDownloadsWriter,
) : DocumentDownloadManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _allStates = MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    private val perIdFlows = mutableMapOf<String, MutableStateFlow<DownloadState>>()
    private val activeJobs = mutableMapOf<String, Job>()

    override fun enqueue(documentId: String) {
        val current = perIdFlows[documentId]?.value
        if (current is DownloadState.Downloading ||
            current is DownloadState.RequestingUrl ||
            current is DownloadState.Saving
        ) return

        val stateFlow = perIdFlows.getOrPut(documentId) { MutableStateFlow(DownloadState.Idle) }
        activeJobs[documentId] = scope.launch { runDownload(documentId, stateFlow) }
    }

    override fun cancel(documentId: String) {
        activeJobs[documentId]?.cancel()
        activeJobs.remove(documentId)
        updateState(documentId, DownloadState.Cancelled)
    }

    override fun observe(documentId: String): StateFlow<DownloadState> =
        perIdFlows.getOrPut(documentId) { MutableStateFlow(DownloadState.Idle) }.asStateFlow()

    override fun observeAll(): StateFlow<Map<String, DownloadState>> = _allStates.asStateFlow()

    private suspend fun runDownload(
        documentId: String,
        stateFlow: MutableStateFlow<DownloadState>,
    ) {
        try {
            // Step 1: request a signed download URL from the backend
            updateState(documentId, DownloadState.RequestingUrl, stateFlow)

            val linkResult = apiService.requestDownloadLink(documentId)
            if (linkResult is Result.Error) {
                updateState(documentId, DownloadState.Failed("Failed to get download link"), stateFlow)
                return
            }
            val link = (linkResult as Result.Success).data

            // Step 2: stream the file from the signed URL, tracking progress
            updateState(documentId, DownloadState.Downloading(0f, 0L, -1L), stateFlow)

            val savedPath = httpClient.prepareGet(link.url) {
                // Signed URL belongs to a CDN — do not leak the user's bearer token
                markAsNoAuth()
            }.execute { response ->
                val totalBytes = response.headers[HttpHeaders.ContentLength]?.toLongOrNull() ?: -1L
                val channel = response.bodyAsChannel()

                writer.write(
                    fileName = link.fileName,
                    mimeType = link.mimeType,
                    source = channel,
                    totalBytes = totalBytes,
                ) { bytesWritten ->
                    val progress =
                        if (totalBytes > 0) bytesWritten.toFloat() / totalBytes.toFloat() else 0f
                    updateState(
                        documentId,
                        DownloadState.Downloading(progress, bytesWritten, totalBytes),
                        stateFlow,
                    )
                }
            }

            updateState(documentId, DownloadState.Completed(savedPath, link.mimeType), stateFlow)
        } catch (e: kotlinx.coroutines.CancellationException) {
            // Job was cancelled via cancel() — state already set to Cancelled there
            throw e
        } catch (e: Exception) {
            updateState(documentId, DownloadState.Failed(e.message ?: "Unknown error"), stateFlow)
        }
    }

    private fun updateState(
        documentId: String,
        state: DownloadState,
        stateFlow: MutableStateFlow<DownloadState>? = perIdFlows[documentId],
    ) {
        stateFlow?.value = state
        _allStates.update { current -> current + (documentId to state) }
    }
}
