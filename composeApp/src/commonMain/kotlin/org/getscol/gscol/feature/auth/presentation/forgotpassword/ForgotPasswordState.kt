package org.getscol.gscol.feature.auth.presentation.forgotpassword

import org.getscol.gscol.feature.auth.domain.model.ForgotPasswordData

data class ForgotPasswordState(
    val phoneNumber: String = "",
    val isLoading: Boolean = false,
    val phoneError: String? = null,
    val errorMessage: String? = null,
    val forgotPasswordData: ForgotPasswordData? = null
)
