package org.getscol.gscol.core.domain

import org.getscol.gscol.core.data.dto.auth.AuthTokenResponse
import org.getscol.gscol.core.domain.Error as Err

sealed interface Result<out D, out E : Err> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : Err>(val error: E) : Result<Nothing, E>
}

inline fun <T, E : Err, R> Result<T, E>.map(map: (T) -> R): Result<R, E> {
    return when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }
}

inline fun <T, E : Err> Result<T, E>.onSuccess(onAction: () -> Unit): Result<T, E> {
    return when (this) {
        is Result.Success -> {
            onAction()
            this
        }

        is Result.Error -> this
    }
}

fun <T, E : Err> Result<T, E>.toData(): AuthTokenResponse? {
    return when (this) {
        is Result.Success -> data as? AuthTokenResponse
        is Result.Error -> null
    }
}


inline fun <T, E : Err> Result<T, E>.onError(onAction: () -> Unit): Result<T, E> {
    return when (this) {
        is Result.Error -> {
            onAction()
            this
        }

        is Result.Success -> this
    }
}

fun <E : Err> Result<*, E>.asUnit(): Result<Unit, E> = map { }  /*{ } == { Unit }*/