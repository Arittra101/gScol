package org.getscol.gscol.auth.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.getscol.gscol.core.data.dto.BaseResponseDto

@Serializable
class OtpReqResponseDto(
    @SerialName("otp_send") val otpSend: Boolean? = null
) : BaseResponseDto()

