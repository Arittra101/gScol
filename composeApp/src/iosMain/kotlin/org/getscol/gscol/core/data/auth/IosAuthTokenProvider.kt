package org.getscol.gscol.core.data.auth

import platform.Foundation.NSUserDefaults

class IosAuthTokenProvider(private val userDefaults: NSUserDefaults) : AuthTokenProvider {

    companion object {
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
    }

    override fun getAccessToken(): String? {
        return userDefaults.stringForKey(KEY_ACCESS_TOKEN)
    }

    override fun getRefreshToken(): String? {
        return userDefaults.stringForKey(KEY_REFRESH_TOKEN)
    }

    override suspend fun saveAccessToken(accessToken: String) {
        userDefaults.setObject(accessToken, KEY_ACCESS_TOKEN)
    }

    override suspend fun saveRefreshToken(refreshToken: String) {
        userDefaults.setObject(refreshToken, KEY_REFRESH_TOKEN)
    }

    override suspend fun clearTokens() {
        userDefaults.removeObjectForKey(KEY_ACCESS_TOKEN)
        userDefaults.removeObjectForKey(KEY_REFRESH_TOKEN)
    }
}