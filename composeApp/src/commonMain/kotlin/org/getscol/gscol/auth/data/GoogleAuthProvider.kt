package org.getscol.gscol.auth.data

import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

interface GoogleAuthProvider {
    suspend fun getGoogleAuthToken() : Result<GoogleAuthToken, DataError.Remote>
}