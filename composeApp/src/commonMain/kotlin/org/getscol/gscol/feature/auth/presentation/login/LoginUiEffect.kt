package org.getscol.gscol.feature.auth.presentation.login

sealed interface LoginUiEffect {
    data class ShowToast(val message: String) : LoginUiEffect
    data class LoginSuccess(val isUserFillupAcademicForm: Int) : LoginUiEffect
}