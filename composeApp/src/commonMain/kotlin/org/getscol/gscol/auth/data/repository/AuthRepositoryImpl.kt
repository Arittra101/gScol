package org.getscol.gscol.auth.data.repository

import org.getscol.gscol.auth.data.AuthTokenProvider
import org.getscol.gscol.auth.data.api_service.AuthApiService
import org.getscol.gscol.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.auth.domain.model.RegistrationResponse
import org.getscol.gscol.auth.domain.model.ResendOtpResponse
import org.getscol.gscol.auth.domain.repository.AuthRepository
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authTokenProvider: AuthTokenProvider
) : AuthRepository {

    override suspend fun register(
        phone: String,
        password: String,
        fullName: String
    ): Result<RegistrationResponse, DataError.Remote> {
        return when (val result = authApiService.register(phone, password, fullName)) {
            is Result.Success -> {
                authTokenProvider.saveAccessToken(
                    accessToken = result.data.data.otpAccessToken,
                )
                Result.Success(result.data)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun login(
        phoneNumber: String,
        password: String
    ): Result<Unit, DataError.Remote> {
        return when (val result = authApiService.login(phoneNumber, password)) {
            is Result.Success -> {
                // Save tokens to storage
                authTokenProvider.saveTokens(
                    accessToken = result.data.accessToken,
                    refreshToken = result.data.refreshToken
                )
                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun verifyOtp(otp: String): Result<Unit, DataError.Remote> {
        return when (val result = authApiService.verifyOtp(otp)) {
            is Result.Success -> {
                // Save access and refresh tokens after successful OTP verification
                authTokenProvider.saveTokens(
                    accessToken = result.data.data.accessToken,
                    refreshToken = result.data.data.refreshToken
                )
                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun resendOtp(): Result<ResendOtpResponse, DataError.Remote> {
        return when (val result = authApiService.resendOtp()) {
            is Result.Success -> {
                // Update the otpAccessToken with the new one
                authTokenProvider.saveAccessToken(
                    accessToken = result.data.data.otpAccessToken
                )
                Result.Success(result.data)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun forgotPassword(phone: String): Result<ForgotPasswordResponse, DataError.Remote> {
        return when (val result = authApiService.forgotPassword(phone)) {
            is Result.Success -> {
                // Save otpAccessToken for OTP verification
                authTokenProvider.saveAccessToken(
                    accessToken = result.data.data.otpAccessToken
                )
                Result.Success(result.data)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }
}