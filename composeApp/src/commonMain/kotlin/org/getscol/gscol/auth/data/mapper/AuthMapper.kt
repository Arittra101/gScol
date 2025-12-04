package org.getscol.gscol.auth.data.mapper

import org.getscol.gscol.auth.data.dto.OtpReqResponseDto
import org.getscol.gscol.auth.data.dto.VerifyResponseDto
import org.getscol.gscol.auth.domain.OtpReqResponse
import org.getscol.gscol.auth.domain.VerifyOtpResponse


fun OtpReqResponseDto.toAuthResponse(): OtpReqResponse {
    return OtpReqResponse(
        otpSend = otpSend
    )
}

fun VerifyResponseDto.toVerifyResponse(): VerifyOtpResponse {
    return VerifyOtpResponse(
        refreshToken = refreshToken,
        accessToken = accessToken
    )
}