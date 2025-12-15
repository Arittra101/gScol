package org.getscol.gscol.auth.domain.validation

/**
 * Centralized validator for authentication-related fields.
 * Provides consistent validation across login, registration, and password reset screens.
 */
object AuthValidator {

    /**
     * Validates a phone number.
     * @param phoneNumber The phone number to validate
     * @param allowEmpty If true, empty phone numbers won't return an error
     * @return Error message if invalid, null if valid
     */
    fun validatePhoneNumber(phoneNumber: String, allowEmpty: Boolean = false): String? {
        val trimmed = phoneNumber.trim()
        return when {
            trimmed.isEmpty() -> if (allowEmpty) null else "Phone number is required"
            trimmed.length < 10 -> "Phone number must be at least 10 digits"
            !trimmed.all { it.isDigit() || it == '+' || it == '-' || it == ' ' } -> 
                "Phone number contains invalid characters"
            else -> null
        }
    }

    /**
     * Validates a password.
     * @param password The password to validate
     * @param allowEmpty If true, empty passwords won't return an error
     * @return Error message if invalid, null if valid
     */
    fun validatePassword(password: String, allowEmpty: Boolean = false): String? {
        return when {
            password.isEmpty() -> if (allowEmpty) null else "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }
    }

    /**
     * Validates a full name.
     * @param fullName The full name to validate
     * @param allowEmpty If true, empty names won't return an error
     * @return Error message if invalid, null if valid
     */
    fun validateFullName(fullName: String, allowEmpty: Boolean = false): String? {
        val trimmed = fullName.trim()
        return when {
            trimmed.isEmpty() -> if (allowEmpty) null else "Full name is required"
            trimmed.length < 2 -> "Full name must be at least 2 characters"
            !trimmed.all { it.isLetter() || it.isWhitespace() } -> 
                "Full name should only contain letters"
            else -> null
        }
    }

    /**
     * Validates an OTP code.
     * @param otp The OTP to validate
     * @param expectedLength Expected length of OTP (default 6)
     * @param allowEmpty If true, empty OTP won't return an error
     * @return Error message if invalid, null if valid
     */
    fun validateOtp(otp: String, expectedLength: Int = 6, allowEmpty: Boolean = false): String? {
        val trimmed = otp.trim()
        return when {
            trimmed.isEmpty() -> if (allowEmpty) null else "OTP is required"
            trimmed.length < expectedLength -> "OTP must be $expectedLength digits"
            !trimmed.all { it.isDigit() } -> "OTP should only contain numbers"
            else -> null
        }
    }

    /**
     * Validates an email address.
     * @param email The email to validate
     * @param allowEmpty If true, empty emails won't return an error
     * @return Error message if invalid, null if valid
     */
    fun validateEmail(email: String, allowEmpty: Boolean = false): String? {
        val trimmed = email.trim()
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return when {
            trimmed.isEmpty() -> if (allowEmpty) null else "Email is required"
            !emailRegex.matches(trimmed) -> "Please enter a valid email address"
            else -> null
        }
    }

    /**
     * Validates password confirmation matches original password.
     * @param password The original password
     * @param confirmPassword The confirmation password
     * @return Error message if they don't match, null if valid
     */
    fun validatePasswordConfirmation(password: String, confirmPassword: String): String? {
        return when {
            confirmPassword.isEmpty() -> "Please confirm your password"
            password != confirmPassword -> "Passwords do not match"
            else -> null
        }
    }
}
