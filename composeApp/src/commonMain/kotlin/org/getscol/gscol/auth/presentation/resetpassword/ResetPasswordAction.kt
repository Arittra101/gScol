package org.getscol.gscol.auth.presentation.resetpassword

sealed interface ResetPasswordAction {
    data class  OnPasswordChange(val password: String) : ResetPasswordAction
    data class  OnConfirmPasswordChange(val password: String) : ResetPasswordAction
    data object OnClickSubmit : ResetPasswordAction
}
