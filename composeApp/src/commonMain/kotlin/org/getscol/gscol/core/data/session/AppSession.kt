package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.Flow
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.StorageKeys

class AppSession(
    private val localStorage: LocalStorage,
) : Session {

    override val isUserLoggedIn: Flow<Boolean>
        get() = localStorage.getFlowBoolean(StorageKeys.IS_USER_LOGGED_IN)

    override suspend fun setUserLoggedIn(value: Boolean) {
        localStorage.setBoolean(StorageKeys.IS_USER_LOGGED_IN, value)
    }

    override val academicFormSubmitTrigger: Flow<Int>
        get() = localStorage.getFlowInt(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT)

    override suspend fun triggerAcademicFormSubmission() {
        val current = localStorage.getInt(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT) ?: 0
        localStorage.setInt(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT, current + 1)
    }

    override suspend fun resetUserPref() {
        localStorage.remove(StorageKeys.ACCESS_TOKEN)
        localStorage.remove(StorageKeys.REFRESH_TOKEN)
        localStorage.remove(StorageKeys.IS_USER_LOGGED_IN)
        localStorage.remove(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT)
    }

}