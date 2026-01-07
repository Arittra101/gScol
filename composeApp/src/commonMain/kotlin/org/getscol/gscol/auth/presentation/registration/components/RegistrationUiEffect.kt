package org.getscol.gscol.auth.presentation.registration.components


sealed interface RegistrationUiEffect {
    data class ShowToast(val message: String) : RegistrationUiEffect
    object RegistrationSuccess : RegistrationUiEffect
    object NavigateBack : RegistrationUiEffect
}