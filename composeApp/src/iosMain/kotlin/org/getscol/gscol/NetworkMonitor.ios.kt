package org.getscol.gscol

import platform.Foundation.NSOperationQueue
import platform.Network.NWPath
import platform.Network.NWPathMonitor
import platform.darwin.dispatch_get_main_queue

/**
 * iOS network monitor using NWPathMonitor to update the common [NetworkStatus].
 * Call NetworkMonitor.start() from your iOS app lifecycle (e.g., App init or AppDelegate).
 */
object NetworkMonitor {
    private var monitor: NWPathMonitor? = null
    private var queue: NSOperationQueue? = null

    fun start() {
        if (monitor != null) return

        // Create the NWPathMonitor
        monitor = NWPathMonitor()

        // Use main queue to marshal updates to the main thread
        queue = NSOperationQueue.mainQueue

        monitor?.setQueue(dispatch_get_main_queue())

        monitor?.setPathUpdateHandler { path: NWPath? ->
            val status = path?.status
            val ok = when (status) {
                NWPathStatusSatisfied -> true
                else -> false
            }
            NetworkStatus.isAvailable.value = ok
        }

        monitor?.start()
    }

    fun stop() {
        monitor?.cancel()
        monitor = null
        queue = null
    }
}

