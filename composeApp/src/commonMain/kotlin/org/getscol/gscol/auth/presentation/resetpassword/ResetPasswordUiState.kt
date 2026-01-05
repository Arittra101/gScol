package org.getscol.gscol.auth.presentation.resetpassword

data class ResetPasswordUiState(
    val password: String = "",
    val confirmPassword: String = "",

    val isPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)