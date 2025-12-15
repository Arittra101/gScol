package org.getscol.gscol.auth.data.api_service

import org.getscol.gscol.auth.domain.model.RegistrationResponse
import org.getscol.gscol.core.data.auth.AuthTokenResponse
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface AuthApiService {
    suspend fun register(
        phone: String,
        password: String,
        fullName: String
    ): Result<RegistrationResponse, DataError.Remote>

    suspend fun login(
        phoneNumber: String,
        password: String
    ): Result<AuthTokenResponse, DataError.Remote>
}