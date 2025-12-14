package org.getscol.gscol.auth.domain.repository

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface AuthRepository {
    suspend fun login(phoneNumber: String, password: String): Result<Unit, DataError.Remote>
}