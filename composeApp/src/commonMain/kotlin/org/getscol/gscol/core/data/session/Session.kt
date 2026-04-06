package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.Flow

interface Session {
    val isUserLoggedIn: Flow<Boolean>
    suspend fun setUserLoggedIn(value: Boolean)
    val academicFormSubmitTrigger: Flow<Int>
    suspend fun triggerAcademicFormSubmission()


    suspend fun resetUserPref()
}


