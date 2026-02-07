package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.data.session.Session.Companion.IS_USER_LOGGED_IN
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.StorageKeys

class AppSession(
    private val localStorage: LocalStorage,
) : Session {

    override val isUserLoggedIn: Flow<Boolean>
        get() = localStorage.getFlowBoolean(IS_USER_LOGGED_IN)

    override suspend fun setUserLoggedIn(value: Boolean) {
        localStorage.setBoolean(IS_USER_LOGGED_IN, value)
    }

    override suspend fun resetUserPref() {
        setUserLoggedIn(false)
        localStorage.remove(StorageKeys.ACCESS_TOKEN)
        localStorage.remove(StorageKeys.REFRESH_TOKEN)
    }

}