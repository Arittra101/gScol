package org.getscol.gscol.auth.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.domain.BaseResponse

@Serializable
data class ResendOtpResponse(
    @SerialName("data") val data: ResendData? = null
) : BaseResponse()


@Serializable
data class ResendData(
    @SerialName("otpAccessToken") val otpAccessToken: String?=null,
    @SerialName("expiresIn") val expiresIn: Int?=null,
    @SerialName("message") val message: String? = null,
    @SerialName("devOtp") val devOtp: String? = null, // For testing only
    @SerialName("retryAfter") val retryAfter: Int
)
