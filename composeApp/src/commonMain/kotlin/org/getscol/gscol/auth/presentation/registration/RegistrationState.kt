package org.getscol.gscol.auth.presentation.registration

import org.getscol.gscol.auth.domain.model.RegistrationData

data class RegistrationState(
    val phoneNumber: String = "",
    val password: String = "",
    val fullName: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val phoneError: String? = null,
    val passwordError: String? = null,
    val fullNameError: String? = null,
    val registrationData: RegistrationData? = null
)
