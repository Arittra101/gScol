package org.getscol.gscol.core.feature_components.upload.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UploadUrlRequestBody(
    @SerialName("fileName") val fileName: String? = null,
    @SerialName("mimeType") val mimeType: String? = null,
    @SerialName("fileSizeBytes") val fileSizeBytes: Long? = null
)