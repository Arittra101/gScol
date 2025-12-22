package org.getscol.gscol.auth.presentation.forgotpassword

import org.getscol.gscol.auth.domain.model.ForgotPasswordData

data class ForgotPasswordState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val phoneError: String? = null,
    val errorMessage: String? = null,
    val forgotPasswordData: ForgotPasswordData? = null
)
