package org.getscol.gscol.feature.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.BaseResponse

@Serializable
data class ForgotPasswordResponse(
    @SerialName("data") val data: ForgotPasswordData? = null
) : BaseResponse()

@Serializable
data class ForgotPasswordData(
    @SerialName("otpAccessToken") val otpAccessToken: String? = null,
    @SerialName("expiresIn") val expiresIn: Int? = null,
    @SerialName("message") val message: String? = null,
    @SerialName("devOtp") val devOtp: String? = null,
    @SerialName("retryAfter") val retryAfter: Int
)
