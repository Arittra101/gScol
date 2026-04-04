package org.getscol.gscol.core.data.session

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.storage.LocalStorage
import org.getscol.gscol.core.data.storage.StorageKeys

class AppSession(
    private val localStorage: LocalStorage,
    private val appScope: CoroutineScope
) : Session {

    private val _isUserLoggedIn = MutableStateFlow(false)
    private val _academicFormSubmitTrigger = MutableStateFlow(0)

    override val isUserLoggedIn: Flow<Boolean> = _isUserLoggedIn
    override val academicFormSubmitTrigger: Flow<Int> = _academicFormSubmitTrigger

    init {
        appScope.launch(Dispatchers.Default) {
            _isUserLoggedIn.value = localStorage.getBoolean(StorageKeys.IS_USER_LOGGED_IN) ?: false
            _academicFormSubmitTrigger.value = localStorage.getInt(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT) ?: 0
        }
    }

    override suspend fun setUserLoggedIn(value: Boolean) {
        _isUserLoggedIn.value = value
        localStorage.setBoolean(StorageKeys.IS_USER_LOGGED_IN, value)
    }

    override suspend fun triggerAcademicFormSubmission() {
        val current = _academicFormSubmitTrigger.value
        _academicFormSubmitTrigger.value = current + 1
        localStorage.setInt(StorageKeys.ACADEMIC_FORM_SUBMIT_COUNT, current + 1)
    }

    override suspend fun resetUserPref() {
        _isUserLoggedIn.value = false
        _academicFormSubmitTrigger.value = 0
        localStorage.clear()
    }

}