package org.getscol.gscol.auth.presentation.resetpassword

sealed interface ResetPasswordAction {
    data class  OnPasswordChange(val password: String) : ResetPasswordAction
    data class  OnPhoneNumberChange(val phoneNumber: String) : ResetPasswordAction
    data class  OnConfirmPasswordChange(val confirmPassword: String) : ResetPasswordAction
    data object OnClickSubmit : ResetPasswordAction
}
