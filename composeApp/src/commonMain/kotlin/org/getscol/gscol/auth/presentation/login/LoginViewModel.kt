package org.getscol.gscol.auth.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.auth.domain.repository.AuthRepository
import org.getscol.gscol.auth.domain.validation.AuthValidator
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber, phoneError = null) }
            }
            is LoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password, passwordError = null) }
            }
            LoginAction.OnLoginClick -> {
                login()
            }
        }
    }

    private fun login() {
        // Clear previous errors
        _state.update { it.copy(errorMessage = null, phoneError = null, passwordError = null) }

        // Validate inputs
        val phoneNumber = _state.value.phoneNumber.trim()
        val password = _state.value.password

        var hasError = false

        // Validate phone number
        AuthValidator.validatePhoneNumber(phoneNumber, allowEmpty = false)?.let { error ->
            _state.update { it.copy(phoneError = error) }
            hasError = true
        }

        // Validate password
        AuthValidator.validatePassword(password, allowEmpty = false)?.let { error ->
            _state.update { it.copy(passwordError = error) }
            hasError = true
        }

        if (hasError) return

        // Perform login
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = authRepository.login(phoneNumber, password)) {
                is Result.Success -> {
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            errorMessage = null
                        )
                    }
                }
                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Remote.REQUEST_TIMEOUT ->
                            "Request timeout. Please try again."
                        DataError.Remote.NO_INTERNET ->
                            "No internet connection. Please check your network."
                        DataError.Remote.SERVER ->
                            "Server error. Please try again later."
                        DataError.Remote.SERIALIZATION ->
                            "Invalid response from server."
                        DataError.Remote.TOO_MANY_REQUESTS ->
                            "Too many requests. Please try again later."
                        else -> "Invalid phone number or password."
                    }
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            errorMessage = errorMessage
                        )
                    }
                }
            }
        }
    }
}
