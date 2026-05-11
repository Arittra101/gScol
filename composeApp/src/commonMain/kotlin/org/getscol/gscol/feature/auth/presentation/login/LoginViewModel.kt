package org.getscol.gscol.feature.auth.presentation.login

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
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.auth.domain.repository.AuthRepository
import org.getscol.gscol.feature.auth.domain.validation.AuthValidator
import org.getscol.gscol.feature.auth.utils.toUiMessage

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val session : Session
) : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state: StateFlow<LoginState> = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<LoginUiEffect>(replay = 0)
    val uiEffect: SharedFlow<LoginUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPhoneNumberChange -> {
                _state.update { it.copy(phoneNumber = action.phoneNumber) }
                validatePhoneNumber(action.phoneNumber)
            }

            is LoginAction.OnPasswordChange -> {
                _state.update { it.copy(password = action.password) }
                validatePassword(action.password)
            }

            is LoginAction.OnLoginClick -> {
                login()
            }

        }
    }

    private fun login() {
        _state.update { it.copy(errorMessage = null) }

        if (!isFormValid()) {
            _state.update { it.copy(errorMessage = "Please fill up all the required fields!") }
            return
        }
        val state = _state.value

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            when (
                val result = authRepository.login(state.phoneNumber, state.password)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isLoginSuccessful = true,
                            errorMessage = null
                        )
                    }
                    val isUserFillUpAcademicForm = session.academicFormSubmitTrigger.value
                    _uiEffect.emit(LoginUiEffect.LoginSuccess(isUserFillUpAcademicForm))
                    session.applicationApplyTrigger()
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

    private fun validatePhoneNumber(phoneNumber: String) {
        val error = AuthValidator.validatePhoneNumber(phoneNumber, allowEmpty = true)
        _state.update { it.copy(phoneError = error) }
    }

    private fun validatePassword(password: String) {
        val error = AuthValidator.validatePassword(password, allowEmpty = true)
        _state.update { it.copy(passwordError = error) }
    }

    private fun isFormValid(): Boolean {
        val state = _state.value

        val phoneError = AuthValidator.validatePhoneNumber(state.phoneNumber.trim(), allowEmpty = false)
        val passwordError = AuthValidator.validatePassword(state.password, allowEmpty = false)

        return listOf(
            phoneError,
            passwordError,
        ).all { it == null }
    }
}
