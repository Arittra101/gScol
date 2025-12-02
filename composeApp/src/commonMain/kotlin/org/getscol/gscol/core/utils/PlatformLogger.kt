package org.getscol.gscol.core.utils

// Platform-specific output — implemented in each target
expect object PlatformLogger {
    fun log(level: LogLevel, tag: String? = null, message: String, throwable: Throwable? = null)
}