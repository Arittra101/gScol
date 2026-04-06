package org.getscol.gscol.feature.auth.domain.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.feature.auth.domain.model.RegistrationResponse
import org.getscol.gscol.feature.auth.domain.model.ResendOtpResponse

interface AuthRepository {
    suspend fun register(phone: String, password: String, fullName: String): Result<RegistrationResponse, DataError>
    suspend fun login(phoneNumber: String, password: String): Result<Unit, DataError>
    suspend fun verifyOtp(otp: String): Result<Unit, DataError>
    suspend fun resendOtp(): Result<ResendOtpResponse, DataError>
    suspend fun forgotPassword(phone: String, newPassword: String): Result<ForgotPasswordResponse, DataError>
}