package org.getscol.gscol.feature.auth.data.repository

import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.domain.asUnit
import org.getscol.gscol.feature.auth.data.AuthTokenProvider
import org.getscol.gscol.feature.auth.data.api_service.AuthApiService
import org.getscol.gscol.feature.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.feature.auth.domain.model.RegistrationResponse
import org.getscol.gscol.feature.auth.domain.model.ResendOtpResponse
import org.getscol.gscol.feature.auth.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val authApiService: AuthApiService,
    private val authTokenProvider: AuthTokenProvider,
    private val session: Session
) : AuthRepository {

    override suspend fun register(
        phone: String,
        password: String,
        fullName: String
    ): Result<RegistrationResponse, DataError.Remote> {
        return when (val result = authApiService.register(phone, password, fullName)) {
            is Result.Success -> {
                authTokenProvider.saveAccessToken(accessToken = result.data.data.otpAccessToken)
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
                val resultData = result.data.data
                val isUserFillUpAcademicForm = resultData?.user?.academicFormStatus?.lowercase() == "completed"

                authTokenProvider.saveTokens(accessToken = resultData?.accessToken, refreshToken = resultData?.refreshToken)

                if(isUserFillUpAcademicForm) {
                    session.incrementAcademicFormSubmitCount()
                }

                Result.Success(Unit)
            }

            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun verifyOtp(otp: String): Result<Unit, DataError.Remote> {
        return when (val result = authApiService.verifyOtp(otp)) {
            is Result.Success -> {
                val resultData = result.data.data
                val isUserFillUpAcademicForm = resultData?.user?.academicFormStatus?.lowercase() == "completed"

                authTokenProvider.saveTokens(accessToken = resultData?.accessToken, refreshToken = resultData?.refreshToken)
                if(isUserFillUpAcademicForm) {
                    session.incrementAcademicFormSubmitCount()
                }

                Result.Success(Unit)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun resendOtp(): Result<ResendOtpResponse, DataError.Remote> {
        return when (val result = authApiService.resendOtp()) {
            is Result.Success -> {
                authTokenProvider.saveAccessToken(accessToken = result.data.data?.otpAccessToken)
                Result.Success(result.data)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun forgotPassword(phone: String, newPassword: String): Result<ForgotPasswordResponse, DataError.Remote> {
        return when (val result = authApiService.forgotPassword(phone, newPassword)) {
            is Result.Success -> {
                authTokenProvider.saveAccessToken(accessToken = result.data.data?.otpAccessToken)
                Result.Success(result.data)
            }
            is Result.Error -> Result.Error(result.error)
        }
    }

    override suspend fun resetPassword(newPassword: String): Result<Unit, DataError.Remote> {
        return authApiService.resetPassword(newPassword).asUnit()
    }

}