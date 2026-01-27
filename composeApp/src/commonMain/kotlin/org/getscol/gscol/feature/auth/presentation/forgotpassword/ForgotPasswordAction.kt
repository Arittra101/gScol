package org.getscol.gscol.feature.auth.presentation.forgotpassword

sealed interface ForgotPasswordAction {
    data class OnPhoneNumberChange(val phoneNumber: String) : ForgotPasswordAction
    data object OnSendOtpClick : ForgotPasswordAction
}
