package org.getscol.gscol.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForgotPasswordResponse(
    @SerialName("status") val status: String,
    @SerialName("message") val message: String,
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data") val data: ForgotPasswordData
)

@Serializable
data class ForgotPasswordData(
    @SerialName("otpAccessToken") val otpAccessToken: String,
    @SerialName("expiresIn") val expiresIn: Int,
    @SerialName("message") val message: String? = null,
    @SerialName("devOtp") val devOtp: String? = null,
    @SerialName("retryAfter") val retryAfter: Int
)
