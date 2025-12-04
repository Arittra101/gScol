package org.getscol.gscol.auth.domain.repository

import org.getscol.gscol.auth.domain.AuthResponse
import org.getscol.gscol.auth.domain.AuthRequest
import org.getscol.gscol.auth.domain.VerifyOtpResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface AuthRepository {
    suspend fun login(authRequest: AuthRequest): Result<VerifyOtpResponse, DataError.Remote>
    suspend fun logout(): Result<AuthResponse, DataError.Remote>
}


