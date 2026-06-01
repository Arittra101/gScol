package org.getscol.gscol

import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Simple cross-platform network status holder.
 * Platform code should update [isAvailable].
 */
object NetworkStatus {
    // true = internet is available/validated, false = no internet
    val isAvailable = MutableStateFlow(true)
}

