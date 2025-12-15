package org.getscol.gscol.auth.presentation.registration

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

class RegistrationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
                validatePhoneNumber(action.phoneNumber)
            }

            is RegistrationAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
                validatePassword(action.password)
            }

            is RegistrationAction.OnFullNameChange -> {
                _state.update { it.copy(fullName = action.fullName) }
                validateFullName(action.fullName)
            }

            RegistrationAction.OnRegisterClick -> {
                register()
            }

            RegistrationAction.OnNavigateToLogin -> {
                // Navigation will be handled in the UI layer
            }
        }
    }

    private fun register() {
        // Clear previous errors
        _state.update {
            it.copy(
                errorMessage = null,
                phoneError = null,
                passwordError = null,
                fullNameError = null
            )
        }

        // Validate inputs
        val phoneNumber = _state.value.phoneNumber.trim()
        val password = _state.value.password
        val fullName = _state.value.fullName.trim()

        var hasError = false

        // Validate full name
        AuthValidator.validateFullName(fullName, allowEmpty = false)?.let { error ->
            _state.update { it.copy(fullNameError = error) }
            hasError = true
        }

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

        // Perform registration
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (val result = authRepository.register(phoneNumber, password, fullName)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registrationData = result.data.data,
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

                        else -> "Registration failed. Please try again."
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

    private fun validateFullName(fullName: String) {
        val error = AuthValidator.validateFullName(fullName, allowEmpty = true)
        _state.update { it.copy(fullNameError = error) }
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        val error = AuthValidator.validatePhoneNumber(phoneNumber, allowEmpty = true)
        _state.update { it.copy(phoneError = error) }
    }

    private fun validatePassword(password: String) {
        val error = AuthValidator.validatePassword(password, allowEmpty = true)
        _state.update { it.copy(passwordError = error) }
    }
}
