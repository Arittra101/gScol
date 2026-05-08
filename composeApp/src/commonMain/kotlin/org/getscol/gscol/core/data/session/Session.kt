package org.getscol.gscol.core.data.session

import kotlinx.coroutines.flow.Flow

interface Session {
    val isUserLoggedIn: Flow<Boolean>
    suspend fun setUserLoggedIn(value: Boolean)
    val academicFormSubmitTrigger: Flow<Int>
    suspend fun triggerAcademicFormSubmission()

    val userFullName: Flow<String?>
    val userJoinedAt: Flow<Int?>
    suspend fun setUserProfile(fullName: String?, joinedAt: Int?)

    suspend fun resetUserPref()
}
