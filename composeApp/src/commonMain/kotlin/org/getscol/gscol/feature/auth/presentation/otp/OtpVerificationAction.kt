package org.getscol.gscol.feature.auth.presentation.otp

sealed interface OtpVerificationAction {
    data class OnOtpChange(val otp: String) : OtpVerificationAction
    data object OnVerifyClick : OtpVerificationAction
    data object OnResendClick : OtpVerificationAction
}
