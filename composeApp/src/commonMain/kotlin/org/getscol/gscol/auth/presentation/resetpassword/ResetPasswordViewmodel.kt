package org.getscol.gscol.auth.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BufferOverflow
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
import org.getscol.gscol.auth.utils.toUiMessage
import org.getscol.gscol.core.domain.Result

class ResetPasswordViewmodel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResetPasswordUiState())
    val state: StateFlow<ResetPasswordUiState> = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<ResetPasswordUiEffect>(replay = 0)
    val uiEffect: SharedFlow<ResetPasswordUiEffect> = _uiEffect.asSharedFlow()


    fun onAction(action: ResetPasswordAction) {
        when (action) {

            is ResetPasswordAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
                validatePassword(action.password)
            }

            is ResetPasswordAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
                validatePhoneNumber(_state.value.phoneNumber)
            }

            is ResetPasswordAction.OnConfirmPasswordChange -> {
                _state.update { it.copy(confirmPassword = action.confirmPassword) }
                validateConfirmPassword(state.value.password, action.confirmPassword)
            }

            is ResetPasswordAction.OnClickSubmit -> {
                submitResetPassword()
            }

        }

    }

    private fun submitResetPassword() {
        _state.update { it.copy(errorMessage = null) }
        if (!isFormValid()) {
            _state.update { it.copy(errorMessage = "Please complete all the required fields!") }
            return
        }


        if(AuthValidator.validatePasswordConfirmation(_state.value.password, _state.value.confirmPassword)!=null){
            _state.update { it.copy(errorMessage = "Password Don't Match") }
            return
        }

        val state = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.forgotPassword(state.phoneNumber,state.password)
            when(result) {
                is Result.Success -> {
                    _state.update { it.copy(isLoading = false, errorMessage = null) }
                    _uiEffect.emit(ResetPasswordUiEffect.ShowToast("Password reset successfully!"))
                    _uiEffect.emit(ResetPasswordUiEffect.PasswordResetSuccess)
                }
                is Result.Error -> { _state.update { it.copy(isLoading = false, errorMessage = result.error.toUiMessage()) } }
            }
        }
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
        val passwordError = AuthValidator.validatePassword(state.password, allowEmpty = false)
        val confirmPasswordError = AuthValidator.validatePassword(state.confirmPassword, allowEmpty = false)
        return listOf(passwordError,confirmPasswordError).all { it == null }
    }

}

