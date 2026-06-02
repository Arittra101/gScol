package org.getscol.gscol.core.utils

enum class LogLevel { DEBUG, INFO, WARN, ERROR }

object AppLogger {
    // runtime configuration
    private var enabled: Boolean = true
    private var currentLevel: LogLevel = LogLevel.DEBUG

    /** Read-only access so other components (e.g. HttpClientFactory) can gate behaviour. */
    val isEnabled: Boolean get() = enabled

    fun setLogLevel(level: LogLevel) {
        currentLevel = level
    }

    fun enableLogging() {
        enabled = true
    }

    fun disableLogging() {
        enabled = false
    }

    /**
     * Log debug message
     * @param tag Optional tag (if null, platform will try to extract from stack trace)
     * @param message Message to log
     */
    fun d(tag: String? = null, message: String) {
        if (!enabled || LogLevel.DEBUG.ordinal < currentLevel.ordinal) return
        PlatformLogger.log(
            LogLevel.DEBUG,
            tag,
            message,
            null
        )
    }

    /**
     * Log info message
     * @param tag Optional tag (if null, platform will try to extract from stack trace)
     * @param message Message to log
     */
    fun i(tag: String? = null, message: String) {
        if (!enabled || LogLevel.INFO.ordinal < currentLevel.ordinal) return
        PlatformLogger.log(
            LogLevel.INFO,
            tag,
            message,
            null
        )
    }

    /**
     * Log warning message
     * @param tag Optional tag (if null, platform will try to extract from stack trace)
     * @param message Message to log
     * @param throwable Optional throwable to log
     */
    fun w(tag: String? = null, message: String, throwable: Throwable? = null) {
        if (!enabled || LogLevel.WARN.ordinal < currentLevel.ordinal) return
        PlatformLogger.log(LogLevel.WARN, tag, message, throwable)
    }

    /**
     * Log error message
     * @param tag Optional tag (if null, platform will try to extract from stack trace)
     * @param message Message to log
     * @param throwable Optional throwable to log
     * Note: ERROR logs are always logged if enabled, regardless of current log level
     */
    fun e(tag: String? = null, message: String, throwable: Throwable? = null) {
        // ERROR should always be logged if enabled, regardless of level
        if (!enabled) return
        PlatformLogger.log(LogLevel.ERROR, tag, message, throwable)
    }

    /**
     * Convenience functions that automatically extract class name from stack trace
     * These allow you to call AppLogger.d("message") without specifying a tag
     */

    /**
     * Log debug message with automatic tag extraction from stack trace
     */
    fun d(message: String) {
        d(null, message)
    }

    /**
     * Log info message with automatic tag extraction from stack trace
     */
    fun i(message: String) {
        i(null, message)
    }

    /**
     * Log warning message with automatic tag extraction from stack trace
     */
    fun w(message: String, throwable: Throwable? = null) {
        w(null, message, throwable)
    }

    /**
     * Log error message with automatic tag extraction from stack trace
     */
    fun e(message: String, throwable: Throwable? = null) {
        e(null, message, throwable)
    }
}