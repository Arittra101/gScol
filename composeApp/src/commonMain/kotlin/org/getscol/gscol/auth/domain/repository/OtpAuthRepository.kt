package org.getscol.gscol.auth.domain.repository

import org.getscol.gscol.auth.domain.AuthRequest
import org.getscol.gscol.auth.domain.OtpReqResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface OtpAuthRepository : AuthRepository {
    suspend fun requestOtp(authRequest: AuthRequest): Result<OtpReqResponse, DataError.Remote>
}