package org.getscol.gscol.core.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class BaseResponseDto(
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("status_code") val statusCode: Int? = null,
)