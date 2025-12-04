package org.getscol.gscol.auth.domain

import kotlinx.serialization.SerialName


/*AuthResponse Model Class*/
data class AuthResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

data class VerifyOtpResponse(
    val accessToken: String? = null,
    val refreshToken: String? = null
)

data class OtpReqResponse(
    val otpSend: Boolean? = null
)



/*Auth Request Model Class*/
open class AuthRequest

data class VerifyOtpRequestData(
    @SerialName("otp") val otp: String
) : AuthRequest()


data class OtpRequestData(
    @SerialName("phone_number") val phoneNumber: String
) : AuthRequest()
