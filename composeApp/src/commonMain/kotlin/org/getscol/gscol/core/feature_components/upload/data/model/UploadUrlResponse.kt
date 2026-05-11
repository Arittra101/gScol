package org.getscol.gscol.core.feature_components.upload.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.BaseResponse

@Serializable
data class UploadUrlResponse(
    @SerialName("data") val data: UploadUrlData? = null
): BaseResponse()

@Serializable
data class UploadUrlData(
    @SerialName("documentId") val documentId: String? = null,
    @SerialName("documentVersionId") val documentVersionId: String? = null,
    @SerialName("uploadUrl") val uploadUrl: String? = null,
)
