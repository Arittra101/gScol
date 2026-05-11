package org.getscol.gscol.feature.profile.data.api_service

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface DocumentDownloadApiService {
    suspend fun requestDownloadLink(documentId: String): Result<DownloadLinkResponse, DataError.Remote>
}

@Serializable
data class DownloadLinkResponse(
    @SerialName("url") val url: String,
    @SerialName("fileName") val fileName: String,
    @SerialName("mimeType") val mimeType: String,
    @SerialName("expiresAt") val expiresAt: String? = null,
)
