package org.getscol.gscol.feature.auth.presentation.registration

sealed interface RegistrationAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : RegistrationAction
    data class OnPasswordChange(val password: String) : RegistrationAction
    data class OnFullNameChange(val fullName: String) : RegistrationAction
    data class OnConfirmPassWordChange(val confirmPassword: String) : RegistrationAction
    data class OnTermsAcceptedChange(val accepted: Boolean) : RegistrationAction
    data object OnRegisterClick : RegistrationAction
    data object OnNavigateToLogin : RegistrationAction
}
