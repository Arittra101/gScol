package org.getscol.gscol.core.data.auth

import android.content.SharedPreferences
import androidx.core.content.edit

class AndroidAuthTokenProvider(private val prefs: SharedPreferences) : AuthTokenProvider {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }

    override fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        prefs.edit { putString(KEY_ACCESS_TOKEN, accessToken) }
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        prefs.edit { putString(KEY_REFRESH_TOKEN, refreshToken) }
    }

    override suspend fun clearTokens() {
        prefs.edit { remove(KEY_ACCESS_TOKEN) }
        prefs.edit { remove(KEY_REFRESH_TOKEN) }
    }

    override suspend fun saveTokens(accessToken: String?, refreshToken: String?) {
        prefs.edit { putString(KEY_ACCESS_TOKEN, accessToken) }
        prefs.edit { putString(KEY_REFRESH_TOKEN, refreshToken) }
    }
}