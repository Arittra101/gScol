package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationResponse(
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data") val data: RegistrationData
)

@Serializable
data class RegistrationData(
    @SerialName("otpAccessToken") val otpAccessToken: String,
    @SerialName("expiresIn") val expiresIn: Int,
    @SerialName("message") val message: String? = null,
    @SerialName("devOtp") val devOtp: String? = null, // For testing only
    @SerialName("retryAfter") val retryAfter: Int
)
