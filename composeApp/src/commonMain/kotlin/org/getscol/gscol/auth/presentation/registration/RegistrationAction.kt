package org.getscol.gscol.auth.presentation.registration

sealed interface RegistrationAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : RegistrationAction
    data class OnPasswordChange(val password: String) : RegistrationAction
    data class OnFullNameChange(val fullName: String) : RegistrationAction
    data object OnRegisterClick : RegistrationAction
    data object OnNavigateToLogin : RegistrationAction
}
