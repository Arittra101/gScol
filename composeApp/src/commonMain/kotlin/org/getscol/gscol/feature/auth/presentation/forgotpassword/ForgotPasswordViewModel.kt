package org.getscol.gscol.feature.auth.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.getscol.gscol.feature.auth.domain.repository.AuthRepository
import org.getscol.gscol.feature.auth.domain.validation.AuthValidator

class ForgotPasswordViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state: StateFlow<ForgotPasswordState> = _state.asStateFlow()

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
                validatePhoneNumber(action.phoneNumber)
            }

            ForgotPasswordAction.OnSendOtpClick -> {
                sendOtp()
            }
        }
    }

    private fun validatePhoneNumber(phoneNumber: String) {
        val error = AuthValidator.validatePhoneNumber(phoneNumber, allowEmpty = true)
        _state.update { it.copy(phoneError = error) }
    }

    private fun sendOtp() {
        /*    // Clear previous errors
            _state.update { it.copy(errorMessage = null, phoneError = null) }

            // Validate phone number
            val phoneNumber = _state.value.phoneNumber.trim()
            AuthValidator.validatePhoneNumber(phoneNumber, allowEmpty = false)?.let { error ->
                _state.update { it.copy(phoneError = error) }
                return
            }

            // Send OTP
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                when (val result = authRepository.forgotPassword(phoneNumber)) {
                    is Result.Success -> {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                forgotPasswordData = result.data.data,
                                errorMessage = null
                            )
                        }
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
            }*/
    }
}
