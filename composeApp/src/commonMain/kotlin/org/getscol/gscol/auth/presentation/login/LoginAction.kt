package org.getscol.gscol.auth.presentation.login

sealed interface LoginAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : LoginAction
    data class OnPasswordChange(val password: String) : LoginAction
    data object OnLoginClick : LoginAction
}
