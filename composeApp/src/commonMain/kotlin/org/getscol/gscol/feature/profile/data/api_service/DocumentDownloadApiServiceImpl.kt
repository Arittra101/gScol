package org.getscol.gscol.feature.profile.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class DocumentDownloadApiServiceImpl(
    private val client: HttpClient,
) : DocumentDownloadApiService {

    override suspend fun requestDownloadLink(
        documentId: String,
    ): Result<DocumentDownloadUrlResponse, DataError.Remote> = safeApiCall {
        client.get("leads/profile/documents/$documentId")
    }
}
