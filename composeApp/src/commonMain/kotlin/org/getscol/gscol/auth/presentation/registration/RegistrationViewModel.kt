package org.getscol.gscol.auth.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.auth.domain.repository.AuthRepository
import org.getscol.gscol.auth.domain.validation.AuthValidator
import org.getscol.gscol.auth.presentation.registration.components.RegistrationUiEffect
import org.getscol.gscol.auth.utils.toUiMessage
import org.getscol.gscol.core.domain.Result

class RegistrationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    private val _uiEffectState = MutableSharedFlow<RegistrationUiEffect>()
    val uiEffectState: SharedFlow<RegistrationUiEffect> = _uiEffectState.asSharedFlow()


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

            is RegistrationAction.OnConfirmPassWordChange -> {
                _state.update { it.copy(confirmPassword = action.confirmPassword) }
                validateConfirmPassword(state.value.password, action.confirmPassword)
            }

            is RegistrationAction.OnTermsAcceptedChange -> {
                _state.update { it.copy(isTermsAccepted = action.accepted) }
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
        _state.update { it.copy(errorMessage = null) }

        if (!isFormValid()) {
            _state.update { it.copy(errorMessage = "Please fill up all the required fields!") }
            return
        }
        if (!state.value.isTermsAccepted) {
            _state.update {
                it.copy(errorMessage = "Please accept Terms & Privacy Policy")
            }
            return
        }

        if(AuthValidator.validatePasswordConfirmation(_state.value.password, _state.value.confirmPassword)!=null){
            _state.update { it.copy(errorMessage = "Password Don't Match") }
            return
        }

        val state = _state.value
        // Perform registration
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            when (
                val result = authRepository.register(
                    state.phoneNumber,
                    state.password,
                    state.fullName,
                )
            ) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registrationData = result.data.data,
                            errorMessage = null
                        )
                    }
                    _uiEffectState.emit(RegistrationUiEffect.RegistrationSuccess)
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = result.error.toUiMessage()
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

    private fun validateConfirmPassword(password: String, confirmPassword: String) {
        val error = AuthValidator.validatePasswordConfirmation(password, confirmPassword)
        _state.update { it.copy(confirmPasswordError = error) }
    }

    private fun isFormValid(): Boolean {
        val state = _state.value
        val fullNameError =
            AuthValidator.validateFullName(state.fullName.trim(), allowEmpty = false)

        val phoneError =
            AuthValidator.validatePhoneNumber(state.phoneNumber.trim(), allowEmpty = false)

        val passwordError =
            AuthValidator.validatePassword(state.password, allowEmpty = false)

        val confirmPasswordError =
            AuthValidator.validatePasswordConfirmation(
                state.password,
                state.confirmPassword
            )

        return listOf(
            fullNameError,
            phoneError,
            passwordError,
            confirmPasswordError
        ).all { it == null }
    }
}
