package org.getscol.gscol.feature.wishlist

import org.getscol.gscol.core.domain.DataError

fun DataError.asWishlistUiMessage(): String {
    return when (this) {
        is DataError.RemoteMessage -> message ?: "Something went wrong"
        DataError.Remote.REQUEST_TIMEOUT -> "Request timed out"
        DataError.Remote.NO_INTERNET -> "No internet connection"
        DataError.Remote.SERVER -> "Server error"
        DataError.Remote.SERIALIZATION -> "Could not read response"
        DataError.Remote.TOO_MANY_REQUESTS -> "Too many requests"
        DataError.Remote.UNKNOWN -> "Something went wrong"
    }
}
