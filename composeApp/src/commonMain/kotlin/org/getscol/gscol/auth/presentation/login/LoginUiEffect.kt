package org.getscol.gscol.auth.presentation.login

sealed interface LoginUiEffect {
    data class ShowToast(val message: String) : LoginUiEffect
    object LoginSuccess : LoginUiEffect
    object NavigateBack : LoginUiEffect
}