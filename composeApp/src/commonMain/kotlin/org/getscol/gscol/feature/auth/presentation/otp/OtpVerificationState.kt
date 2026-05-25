package org.getscol.gscol.feature.auth.presentation.otp

data class OtpVerificationState(
    var otp: String = "",
    val isLoading: Boolean = false,
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val errorMessage: String? = null,
    val otpError: String? = null,
    val isVerificationSuccessful: Boolean = false,
    val isAcademicFormFillup : Boolean = false,
    
    // Timer states
    val tokenExpirationSeconds: Int = 60,
    val isTokenExpired: Boolean = false,
    val canResend: Boolean = false,
    val resendingOtp: Boolean = false,
)
