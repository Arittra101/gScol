package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OtpVerificationResponse(
    @SerialName("status") val status: String? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("statusCode") val statusCode: Int? = null,
    @SerialName("data") val data: OtpVerificationData? = null
)

@Serializable
data class OtpVerificationData(
    @SerialName("user") val user: User? = null,
    @SerialName("accessToken") val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String
)
