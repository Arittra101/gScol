package org.getscol.gscol.feature.auth.data

import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.StorageKeys
import org.getscol.gscol.core.utils.AppLogger

class AuthTokenProvider(
    private val localStorage: LocalStorage,
    private val session: Session
) {
    suspend fun saveAccessToken(accessToken: String?) {
        if (accessToken == null) return
        localStorage.setString(StorageKeys.ACCESS_TOKEN, accessToken)
        session.setUserLoggedIn(true)
    }

    suspend fun saveRefreshToken(refreshToken: String?) {
        if (refreshToken == null) return
        localStorage.setString(StorageKeys.REFRESH_TOKEN, refreshToken)
    }

    suspend fun saveTokens(accessToken: String?, refreshToken: String?) {
        saveAccessToken(accessToken)
        saveRefreshToken(refreshToken)
    }

    suspend fun getAccessToken(): String? {
        return localStorage.getString(StorageKeys.ACCESS_TOKEN)
    }

    suspend fun getRefreshToken(): String? {
        return localStorage.getString(StorageKeys.REFRESH_TOKEN)
    }

    suspend fun clearTokens() {
        AppLogger.d("clearTokens token ")
        localStorage.remove(StorageKeys.ACCESS_TOKEN)
        localStorage.remove(StorageKeys.REFRESH_TOKEN)
        session.setUserLoggedIn(false)
    }

    suspend fun isUserLogin(): Boolean {
        return localStorage.getString(StorageKeys.ACCESS_TOKEN) != null
    }
}