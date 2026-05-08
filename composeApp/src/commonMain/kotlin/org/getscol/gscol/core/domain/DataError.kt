package org.getscol.gscol.core.domain

sealed interface DataError : Error {
    enum class Remote : DataError {
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN,
    }

    data class RemoteMessage(val message: String?, val statusCode: Int?) : DataError
}

sealed interface DocumentError : Error {
    data object PdfUploadError : DocumentError
    data object PdfDownloadError : DocumentError
}