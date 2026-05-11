package org.getscol.gscol.feature.profile.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class DocumentDownloadApiServiceImpl(
    private val client: HttpClient,
) : DocumentDownloadApiService {

    override suspend fun requestDownloadLink(
        documentId: String,
    ): Result<DownloadLinkResponse, DataError.Remote> = safeApiCall {
        client.post("students/me/documents/$documentId/download-link") {
            contentType(ContentType.Application.Json)
        }
    }
}
