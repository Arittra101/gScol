package org.getscol.gscol.core.data.auth

interface AuthTokenProvider {
    fun getAccessToken(): String?

    fun getRefreshToken(): String?
    suspend fun saveAccessToken(accessToken: String)

    suspend fun saveRefreshToken(refreshToken: String)
    suspend fun clearTokens()
}