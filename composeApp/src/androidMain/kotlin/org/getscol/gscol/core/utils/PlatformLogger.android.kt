@file:Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")

package org.getscol.gscol.core.utils

import timber.log.Timber

actual object PlatformLogger {
    actual fun log(level: LogLevel, tag: String?, message: String, throwable: Throwable?) {
        val effectiveTag = tag ?: "SCOL_LOGGER"
        when (level) {
            LogLevel.DEBUG -> Timber.tag(effectiveTag).d(message)
            LogLevel.INFO -> Timber.tag(effectiveTag).i(message)
            LogLevel.WARN -> {
                if (throwable != null) Timber.tag(effectiveTag)
                    .w(throwable, message) else Timber.tag(effectiveTag).w(message)
            }

            LogLevel.ERROR -> {
                if (throwable != null) Timber.tag(effectiveTag)
                    .e(throwable, message) else Timber.tag(effectiveTag).e(message)
            }
        }
    }
}