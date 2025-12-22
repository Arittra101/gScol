package org.getscol.gscol.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.auth.domain.repository.AuthRepository
import org.getscol.gscol.auth.domain.validation.AuthValidator
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result

class OtpVerificationViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(OtpVerificationState())
    val state: StateFlow<OtpVerificationState> = _state.asStateFlow()

    private var expirationTimerJob: Job? = null
    private var resendTimerJob: Job? = null

    init {
        startTimers()
    }

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
            }
        }
    }

    private fun startTimers() {
        // Start expiration countdown (3 minutes)
        expirationTimerJob?.cancel()
        expirationTimerJob = viewModelScope.launch {
            var secondsRemaining = _state.value.tokenExpirationSeconds
            while (secondsRemaining > 0) {
                delay(1000)
                secondsRemaining--
                _state.update { 
                    it.copy(
                        tokenExpirationSeconds = secondsRemaining,
                        isTokenExpired = secondsRemaining == 0
                    ) 
                }
            }
        }

        // Start resend countdown (60 seconds)
        resendTimerJob?.cancel()
        resendTimerJob = viewModelScope.launch {
            var secondsRemaining = _state.value.resendAvailableSeconds
            while (secondsRemaining > 0) {
                delay(1000)
                secondsRemaining--
                _state.update { 
                    it.copy(
                        resendAvailableSeconds = secondsRemaining,
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
                            errorMessage = null
                        )
                    }
                    // Stop timers on success
                    expirationTimerJob?.cancel()
                    resendTimerJob?.cancel()
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

                        else -> "Invalid OTP. Please try again."
                    }
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            errorMessage = errorMessage
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
            _state.update { it.copy(isResending = true, errorMessage = null) }

            when (val result = authRepository.resendOtp()) {
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            isResending = false,
                            otp = "", // Clear OTP input
                            otpError = null,
                            errorMessage = null,
                            // Reset timers with new values from response
                            tokenExpirationSeconds = result.data.data.expiresIn,
                            resendAvailableSeconds = result.data.data.retryAfter,
                            isTokenExpired = false,
                            canResend = false
                        )
                    }
                    // Restart timers
                    startTimers()
                }

                is Result.Error -> {
                    val errorMessage = when (result.error) {
                        DataError.Remote.REQUEST_TIMEOUT ->
                            "Request timeout. Please try again."

                        DataError.Remote.NO_INTERNET ->
                            "No internet connection. Please check your network."

                        DataError.Remote.SERVER ->
                            "Server error. Please try again later."

                        DataError.Remote.TOO_MANY_REQUESTS ->
                            "Too many requests. Please try again later."

                        else -> "Failed to resend OTP. Please try again."
                    }
                    _state.update {
                        it.copy(
                            isResending = false,
                            errorMessage = errorMessage
                        )
                    }
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
