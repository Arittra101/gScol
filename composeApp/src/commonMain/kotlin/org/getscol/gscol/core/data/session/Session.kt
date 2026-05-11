package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface Session {
    val isUserLoggedIn: StateFlow<Boolean>
    val triggerApplicationListScreen: SharedFlow<Unit>
    suspend fun setUserLoggedIn(value: Boolean)
    val academicFormSubmitTrigger: StateFlow<Int>
    suspend fun triggerAcademicFormSubmission()

    suspend fun applicationApplyTrigger()

    suspend fun resetUserPref()
}


