package org.getscol.gscol

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

/**
 * iOS network monitor.
 *
 * NOTE:
 * Your current Kotlin/Native iOS bindings don't expose `NWPathMonitor`,
 * so we can't rely on it here. Instead, we update [NetworkStatus] by doing
 * a lightweight periodic request.
 */
object NetworkMonitor {
    private const val PING_URL = "https://www.google.com/generate_204"
    private const val CHECK_INTERVAL_MS = 5_000L
    private const val CHECK_TIMEOUT_MS = 3_000L

    private var client: HttpClient? = null
    private var job: Job? = null

    fun start() {
        if (job != null) return

        val newClient = HttpClient(Darwin) {
            install(HttpTimeout) {
                requestTimeoutMillis = CHECK_TIMEOUT_MS
                socketTimeoutMillis = CHECK_TIMEOUT_MS
            }
        }

        client = newClient

        job = CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            while (currentCoroutineContext().isActive) {
                NetworkStatus.isAvailable.value = isInternetAvailable(newClient)
                delay(CHECK_INTERVAL_MS)
            }
        }
    }

    fun stop() {
        job?.cancel()
        job = null
        client?.close()
        client = null
    }

    private suspend fun isInternetAvailable(httpClient: HttpClient): Boolean {
        return try {
            withTimeout(CHECK_TIMEOUT_MS) {
                val response = httpClient.get(PING_URL)
                response.status.value in 200..299
            }
        } catch (_: Exception) {
            false
        }
    }
}
