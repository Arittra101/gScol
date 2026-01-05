package org.getscol.gscol.auth.presentation.otp

data class OtpVerificationState(
    val otp: String = "",
    val isLoading: Boolean = false,
    val isVerifying: Boolean = false,
    val isResending: Boolean = false,
    val errorMessage: String? = null,
    val otpError: String? = null,
    val isVerificationSuccessful: Boolean = false,
    
    // Timer states
    val tokenExpirationSeconds: Int = 13, // 3 minutes = 180 seconds
    val resendAvailableSeconds: Int = 7, // Can resend after 60 seconds
    val isTokenExpired: Boolean = false,
    val canResend: Boolean = false,
    val resendingOtp: Boolean = false,
)
