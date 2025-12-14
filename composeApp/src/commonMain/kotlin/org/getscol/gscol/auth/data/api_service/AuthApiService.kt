package org.getscol.gscol.auth.data.api_service

import org.getscol.gscol.core.data.auth.AuthTokenResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface AuthApiService {
    suspend fun login(phoneNumber: String, password: String): Result<AuthTokenResponse, DataError.Remote>
}