package org.getscol.gscol.auth.data.repository

import org.getscol.gscol.auth.data.mapper.toAuthResponse
import org.getscol.gscol.auth.data.mapper.toVerifyResponse
import org.getscol.gscol.auth.data.network.OtpAuthService
import org.getscol.gscol.auth.domain.AuthRequest
import org.getscol.gscol.auth.domain.AuthResponse
import org.getscol.gscol.auth.domain.OtpReqResponse
import org.getscol.gscol.auth.domain.OtpRequestData
import org.getscol.gscol.auth.domain.VerifyOtpRequestData
import org.getscol.gscol.auth.domain.VerifyOtpResponse
import org.getscol.gscol.auth.domain.repository.OtpAuthRepository
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.map

class OtpAuthRepositoryImp(private val otpAuthService: OtpAuthService) : OtpAuthRepository {

    override suspend fun requestOtp(authRequest: AuthRequest): Result<OtpReqResponse, DataError.Remote> {
        val otpRequestData = authRequest as? OtpRequestData
        if (otpRequestData == null) return Result.Error(DataError.Remote.CAST_ERROR)

        return otpAuthService.requestOtp(otpRequestData).map { it ->
            it.toAuthResponse()
        }
    }

    override suspend fun login(authRequest: AuthRequest): Result<VerifyOtpResponse, DataError.Remote> {
        val verifyOtpRequestData = authRequest as? VerifyOtpRequestData
        if (verifyOtpRequestData == null) return Result.Error(DataError.Remote.CAST_ERROR)

        return otpAuthService.verifyOtp(verifyOtpRequestData).map { it ->
            it.toVerifyResponse()
        }
    }

    override suspend fun logout(): Result<AuthResponse, DataError.Remote> {
        TODO("faisal bhai will implement it")
    }


}
