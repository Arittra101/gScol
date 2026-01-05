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

    private val _uiEffect = MutableSharedFlow<ResetPasswordUiEffect>(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val uiEffect: SharedFlow<ResetPasswordUiEffect> = _uiEffect.asSharedFlow()


    fun onAction(action: ResetPasswordAction) {
        when (action) {

            is ResetPasswordAction.OnPasswordChange -> {
                val passwordError = AuthValidator.validatePassword(_state.value.password, allowEmpty = false)
                _state.update { it.copy(password = action.password) }
                _state.update { it.copy(passwordError = passwordError) }
            }

            is ResetPasswordAction.OnConfirmPasswordChange -> {
                val passwordError = AuthValidator.validatePassword(
                    _state.value.password,
                    allowEmpty = false,
                    confirmPassword = _state.value.password
                )
                _state.update { it.copy(password = action.password) }
                _state.update { it.copy(confirmPasswordError = passwordError) }
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

        val state = _state.value
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val result = authRepository.resetPassword(state.password)
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

    private fun isFormValid(): Boolean {
        val state = _state.value
        val passwordError = AuthValidator.validatePassword(state.password, allowEmpty = false)
        val confirmPasswordError = AuthValidator.validatePassword(state.confirmPassword, allowEmpty = false)
        return listOf(passwordError,confirmPasswordError).all { it == null }
    }

}

