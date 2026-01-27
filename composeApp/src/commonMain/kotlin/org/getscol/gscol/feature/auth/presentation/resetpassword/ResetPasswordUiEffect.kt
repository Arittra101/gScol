package org.getscol.gscol.feature.auth.presentation.resetpassword

sealed interface ResetPasswordUiEffect {
    data class ShowToast(val message: String) : ResetPasswordUiEffect
    object PasswordResetSuccess : ResetPasswordUiEffect
    object NavigateBack : ResetPasswordUiEffect
}