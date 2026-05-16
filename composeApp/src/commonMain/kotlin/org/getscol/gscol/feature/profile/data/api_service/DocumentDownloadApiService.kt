package org.getscol.gscol.feature.profile.data.api_service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.BaseResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface DocumentDownloadApiService {
    suspend fun requestDownloadLink(documentId: String): Result<DocumentDownloadUrlResponse, DataError.Remote>
}

@Serializable
data class DocumentDownloadUrlResponse(
    @SerialName("data") val data: DocumentDownloadUrlData? = null,
) : BaseResponse()

@Serializable
data class DocumentDownloadUrlData(
    @SerialName("url") val url: String,
    @SerialName("fileName") val fileName: String? = null,
    @SerialName("expiresInSeconds") val expiresInSeconds: Long? = null,
)
