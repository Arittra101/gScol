package org.getscol.gscol.feature.auth.domain.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.feature.auth.domain.model.RegistrationResponse
import org.getscol.gscol.feature.auth.domain.model.ResendOtpResponse

interface AuthRepository {
    suspend fun register(phone: String, password: String, fullName: String): Result<RegistrationResponse, DataError.Remote>
    suspend fun login(phoneNumber: String, password: String): Result<Unit, DataError.Remote>
    suspend fun verifyOtp(otp: String): Result<Unit, DataError.Remote>
    suspend fun resendOtp(): Result<ResendOtpResponse, DataError.Remote>
    suspend fun forgotPassword(phone: String, newPassword: String): Result<ForgotPasswordResponse, DataError.Remote>
    suspend fun resetPassword(newPassword: String): Result<Unit, DataError.Remote>
}