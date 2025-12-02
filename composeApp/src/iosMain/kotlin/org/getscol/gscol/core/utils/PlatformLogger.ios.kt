@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.getscol.gscol.core.utils

import platform.Foundation.NSLog

actual object PlatformLogger {
    actual fun log(
        level: LogLevel,
        tag: String?,
        message: String,
        throwable: Throwable?
    ) {
        val effectiveTag = tag ?: "SCOL_LOGGER"
        val levelPrefix = when (level) {
            LogLevel.DEBUG -> "🔵 [DEBUG]"
            LogLevel.INFO -> "ℹ️ [INFO]"
            LogLevel.WARN -> "⚠️ [WARN]"
            LogLevel.ERROR -> "❌ [ERROR]"
        }

        val base = "$levelPrefix [$effectiveTag] $message"
        val fullMessage = if (throwable != null) {
            val stack = throwable.stackTraceToString()
            "$base\nThrowable: ${throwable::class.simpleName} - ${throwable.message ?: "No message"}\n$stack"
        } else {
            base
        }

        NSLog(fullMessage)
    }
}