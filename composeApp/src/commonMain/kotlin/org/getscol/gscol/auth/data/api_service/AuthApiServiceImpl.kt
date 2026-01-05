package org.getscol.gscol.auth.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.auth.data.authdto.AuthPassResetReqDto
import org.getscol.gscol.auth.data.authdto.AuthPassResetResponseDto
import org.getscol.gscol.auth.domain.model.ForgotPasswordRequest
import org.getscol.gscol.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.auth.domain.model.LoginRequest
import org.getscol.gscol.auth.domain.model.OtpVerificationRequest
import org.getscol.gscol.auth.domain.model.OtpVerificationResponse
import org.getscol.gscol.auth.domain.model.RegistrationRequest
import org.getscol.gscol.auth.domain.model.RegistrationResponse
import org.getscol.gscol.auth.domain.model.ResendOtpResponse
import org.getscol.gscol.core.data.dto.auth.AuthTokenResponse
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class AuthApiServiceImpl(
    private val httpClient: HttpClient
) : AuthApiService {

    override suspend fun register(
        phone: String,
        password: String,
        fullName: String
    ): Result<RegistrationResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/register") {
                contentType(ContentType.Application.Json)
                setBody(
                    RegistrationRequest(
                        phone = phone,
                        password = password,
                        fullName = fullName
                    )
                )
                markAsNoAuth()
            }
        }
    }

    override suspend fun login(
        phoneNumber: String,
        password: String
    ): Result<AuthTokenResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(phoneNumber = phoneNumber, password = password))
                markAsNoAuth()
            }
        }
    }

    override suspend fun verifyOtp(otp: String): Result<OtpVerificationResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/verify-otp") {
                contentType(ContentType.Application.Json)
                setBody(OtpVerificationRequest(otp = otp))
            }
        }
    }

    override suspend fun resendOtp(): Result<ResendOtpResponse, DataError.Remote> {
        return safeApiCall { httpClient.get("auth/resend-otp") {} }
    }

    override suspend fun forgotPassword(phone: String): Result<ForgotPasswordResponse, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/forgot-password") {
                contentType(ContentType.Application.Json)
                setBody(ForgotPasswordRequest(phone = phone))
                markAsNoAuth()
            }
        }
    }

    override suspend fun resetPassword(newPassword: String): Result<AuthPassResetResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("auth/reset-password") {
                contentType(ContentType.Application.Json)
                setBody(AuthPassResetReqDto(newPassword = newPassword))
            }
        }
    }
}