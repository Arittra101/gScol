package org.getscol.gscol.core.domain

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiErrorResponse(
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("statusCode") val statusCode: Int? = null
)