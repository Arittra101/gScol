package org.getscol.gscol.feature.auth.presentation.registration

import org.getscol.gscol.feature.auth.domain.model.RegistrationData

data class RegistrationState(
    val phoneNumber: String = "",
    val password: String = "",
    val fullName: String = "",
    val confirmPassword: String = "",
    val isTermsAccepted: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val fullNameError: String? = null,
    val registrationData: RegistrationData? = null
)
