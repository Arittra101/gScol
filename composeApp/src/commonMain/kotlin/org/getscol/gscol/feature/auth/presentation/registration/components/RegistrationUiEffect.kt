package org.getscol.gscol.feature.auth.presentation.registration.components


sealed interface RegistrationUiEffect {
    object RegistrationSuccess : RegistrationUiEffect
}