package org.getscol.gscol.auth.presentation.forgotpassword

sealed interface ForgotPasswordAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : ForgotPasswordAction
    data object OnSendOtpClick : ForgotPasswordAction
}
