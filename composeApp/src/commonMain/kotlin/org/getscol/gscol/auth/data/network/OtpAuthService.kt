package org.getscol.gscol.auth.data.network

import org.getscol.gscol.auth.data.dto.OtpReqResponseDto
import org.getscol.gscol.auth.data.dto.VerifyResponseDto
import org.getscol.gscol.auth.domain.OtpRequestData
import org.getscol.gscol.auth.domain.VerifyOtpRequestData
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface OtpAuthService {
    suspend fun requestOtp(otpRequestData: OtpRequestData): Result<OtpReqResponseDto, DataError.Remote>
    suspend fun verifyOtp(verifyOtpRequestData: VerifyOtpRequestData): Result<VerifyResponseDto, DataError.Remote>
}