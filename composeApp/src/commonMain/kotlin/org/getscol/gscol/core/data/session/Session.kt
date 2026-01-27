package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.Flow

interface Session {

    companion object {
        const val IS_USER_LOGGED_IN = "IS_USER_LOGGED_IN"
    }
    val isUserLoggedIn: Flow<Boolean>
    suspend fun setUserLoggedIn(value: Boolean)
}


