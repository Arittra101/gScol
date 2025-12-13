package org.getscol.gscol.auth.presentation.login

data class LoginState(
    val phoneNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val isLoginSuccessful: Boolean = false
)
