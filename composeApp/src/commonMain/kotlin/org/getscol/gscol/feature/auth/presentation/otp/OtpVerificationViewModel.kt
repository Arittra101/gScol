package org.getscol.gscol.feature.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.auth.domain.repository.AuthRepository
import org.getscol.gscol.feature.auth.domain.validation.AuthValidator
import org.getscol.gscol.feature.auth.utils.toUiMessage

class OtpVerificationViewModel(
    private val authRepository: AuthRepository,
    private val session: Session
) : ViewModel() {

    private val _state = MutableStateFlow(OtpVerificationState())
    val state: StateFlow<OtpVerificationState> = _state.asStateFlow()

    private var expirationTimerJob: Job? = null
    private var resendTimerJob: Job? = null

    init { startOtpExpireTimer() }

    fun onAction(action: OtpVerificationAction) {
        when (action) {
            is OtpVerificationAction.OnOtpChange -> {
                _state.update { it.copy(otp = action.otp) }
                validateOtp(action.otp)
            }

            OtpVerificationAction.OnVerifyClick -> {
                verifyOtp()
            }

            OtpVerificationAction.OnResendClick -> {
                resendOtp()
                startResendOtpTimer()
                startOtpExpireTimer()
            }
        }
    }

    private fun startResendOtpTimer(){
        resendTimerJob?.cancel()
        resendTimerJob = viewModelScope.launch {
            var secondsRemaining = _state.value.resendAvailableSeconds
            if(secondsRemaining == 0) secondsRemaining = 20
            while (secondsRemaining > 0) {
                delay(1000)
                secondsRemaining--
                _state.update {
                    it.copy(
                        resendAvailableSeconds = secondsRemaining,
                        resendingOtp = secondsRemaining != 0
                    )
                }
            }
        }
    }

    private fun startOtpExpireTimer() {
        // Start expiration countdown (3 minutes)
        expirationTimerJob?.cancel()
        expirationTimerJob = viewModelScope.launch {
            var secondsRemaining = _state.value.tokenExpirationSeconds
            if(secondsRemaining == 0) secondsRemaining = 30
            while (secondsRemaining > 0) {
                delay(1000)
                secondsRemaining--
                _state.update { 
                    it.copy(
                        tokenExpirationSeconds = secondsRemaining,
                        isTokenExpired = secondsRemaining == 0,
                        canResend = secondsRemaining == 0
                    ) 
                }
            }
        }

    }

    private fun validateOtp(otp: String) {
        val error = AuthValidator.validateOtp(otp, allowEmpty = true)
        _state.update { it.copy(otpError = error) }
    }

    private fun verifyOtp() {
        // Clear previous errors
        _state.update { it.copy(errorMessage = null, otpError = null) }

        // Validate OTP
        val otp = _state.value.otp.trim()
        AuthValidator.validateOtp(otp, allowEmpty = false)?.let { error ->
            _state.update { it.copy(otpError = error) }
            return
        }

        // Check if token expired
        if (_state.value.isTokenExpired) {
            _state.update { it.copy(errorMessage = "OTP has expired. Please request a new one.") }
            return
        }

        // Perform OTP verification
        viewModelScope.launch {
            _state.update { it.copy(isVerifying = true) }

            when (val result = authRepository.verifyOtp(otp)) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerificationSuccessful = true,
                            errorMessage = null,
                            isAcademicFormFillup = session.academicFormSubmitTrigger.first() > 0
                        )
                    }

                    // Stop timers on success
                    expirationTimerJob?.cancel()
                    resendTimerJob?.cancel()
                }

                is Result.Error -> {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            errorMessage = result.error.toUiMessage()
                        )
                    }
                }
            }
        }
    }

    private fun resendOtp() {
        if (!_state.value.canResend) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isTokenExpired = false, errorMessage = null, resendingOtp = true) }

            when (val result = authRepository.resendOtp()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isResending = false,
                            otpError = null,
                            errorMessage = null,
                            isTokenExpired = false,
                        )
                    }
                    // Restart timers
                }

                is Result.Error -> {
                    print("get the error")
                    _state.update {
                        it.copy(
                            isResending = false,
                            errorMessage = result.error.toUiMessage(),
                            resendingOtp = false
                        )
                    }
                    resendTimerJob?.cancel()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        expirationTimerJob?.cancel()
        resendTimerJob?.cancel()
    }
}
