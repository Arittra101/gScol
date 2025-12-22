package org.getscol.gscol.auth.domain.repository

import org.getscol.gscol.auth.domain.model.ForgotPasswordResponse
import org.getscol.gscol.auth.domain.model.RegistrationResponse
import org.getscol.gscol.auth.domain.model.ResendOtpResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface AuthRepository {
    suspend fun register(phone: String, password: String, fullName: String): Result<RegistrationResponse, DataError.Remote>
    suspend fun login(phoneNumber: String, password: String): Result<Unit, DataError.Remote>
    suspend fun verifyOtp(otp: String): Result<Unit, DataError.Remote>
    suspend fun resendOtp(): Result<ResendOtpResponse, DataError.Remote>
    suspend fun forgotPassword(phone: String): Result<ForgotPasswordResponse, DataError.Remote>
}