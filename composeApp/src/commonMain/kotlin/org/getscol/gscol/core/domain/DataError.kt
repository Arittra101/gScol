package org.getscol.gscol.core.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN,
        CAST_ERROR,
        REFRESH_TOKEN_EXPIRED,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND
    }

    enum class Local : DataError {
        DISK_FULL,
        UNKNOWN
    }
}