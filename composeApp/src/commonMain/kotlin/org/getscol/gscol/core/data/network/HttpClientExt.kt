package org.getscol.gscol.core.data.network

import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.statement.HttpResponse
import io.ktor.util.AttributeKey
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import kotlin.coroutines.coroutineContext

// Attribute key for marking requests that don't require authentication
private val NoAuthAttributeKey = AttributeKey<Boolean>("NoAuth")

/**
 * Mark this request as not requiring authentication.
 * Use this for login, registration, and other public endpoints.
 */
fun HttpRequestBuilder.markAsNoAuth() {
    attributes.put(NoAuthAttributeKey, true)
}

/**
 * Check if this request is marked as not requiring authentication.
 */
fun HttpRequestBuilder.isMarkedAsNoAuth(): Boolean {
    return attributes.getOrNull(NoAuthAttributeKey) ?: false
}

suspend inline fun <reified T> safeApiCall(execute: () -> HttpResponse): Result<T, DataError.Remote> {
    val response = try {
        execute()
    } catch (e: SocketTimeoutException) {
        return Result.Error(DataError.Remote.REQUEST_TIMEOUT)
    } catch (e: UnresolvedAddressException) {
        return Result.Error(DataError.Remote.NO_INTERNET)
    } catch (e: Exception) {
        coroutineContext.ensureActive()
        return Result.Error(DataError.Remote.UNKNOWN)
    }

    return responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<T, DataError.Remote> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                Result.Success(response.body<T>())
            } catch (e: NoTransformationFoundException) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        408 -> Result.Error(DataError.Remote.REQUEST_TIMEOUT)
        429 -> Result.Error(DataError.Remote.TOO_MANY_REQUESTS)
        in 500..599 -> Result.Error(DataError.Remote.SERVER)
        else -> Result.Error(DataError.Remote.UNKNOWN)
    }
}