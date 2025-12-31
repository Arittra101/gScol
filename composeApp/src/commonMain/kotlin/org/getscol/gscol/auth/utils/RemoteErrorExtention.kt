package org.getscol.gscol.auth.utils

import org.getscol.gscol.core.domain.DataError

fun DataError.Remote.toUiMessage(): String =
    when (this) {
        DataError.Remote.REQUEST_TIMEOUT ->
            "Request timeout. Please try again."

        DataError.Remote.NO_INTERNET ->
            "No internet connection."

        DataError.Remote.SERVER ->
            "Server error. Please try later."

        DataError.Remote.SERIALIZATION ->
            "Invalid server response."

        DataError.Remote.TOO_MANY_REQUESTS ->
            "Too many requests. Try again later."

        else ->
            "Something went wrong. Please try again."
    }