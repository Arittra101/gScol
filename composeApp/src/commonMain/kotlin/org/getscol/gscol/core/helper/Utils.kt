package org.getscol.gscol.core.helper

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import kotlinx.serialization.json.Json
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.platformEnterTransition
import org.getscol.gscol.platformExitTransition
import org.getscol.gscol.platformPopEnterTransition
import org.getscol.gscol.platformPopExitTransition
import kotlin.jvm.JvmSuppressWildcards
import kotlin.math.roundToInt
import kotlin.reflect.KType


inline fun <reified T : Route> NavGraphBuilder.composableNoAnimation(
    typeMap: Map<KType, @JvmSuppressWildcards NavType<*>> = emptyMap(),
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        typeMap = typeMap,
        enterTransition = { platformEnterTransition() },
        exitTransition = { platformExitTransition() },
        popEnterTransition = { platformPopEnterTransition() },
        popExitTransition = { platformPopExitTransition() },
        content = content
    )
}

/**
 * Maps ISO 4217 codes (e.g. "USD") to a display symbol (e.g. "$").
 * Unknown or non-3-letter codes are returned unchanged (trimmed).
 */
fun iso4217CurrencySymbol(code: String): String {
    val trimmed = code.trim()
    if (trimmed.isEmpty()) return ""
    val upper = trimmed.uppercase()
    if (upper.length != 3 || !upper.all { it.isLetter() }) return trimmed
    return Iso4217CurrencySymbols[upper] ?: upper
}

private val Iso4217CurrencySymbols = mapOf(
    "USD" to "$",
    "EUR" to "€",
    "GBP" to "£",
    "JPY" to "¥",
    "AUD" to "A$",
    "CAD" to "C$",
    "CHF" to "CHF",
    "SGD" to "S$",
    "KRW" to "₩",
    "CNY" to "¥",
    "HKD" to "HK$",
    "NZD" to "NZ$",
    "INR" to "₹",
    "SEK" to "kr",
    "NOK" to "kr",
    "DKK" to "kr",
    "PLN" to "zł",
    "THB" to "฿",
    "MYR" to "RM",
    "IDR" to "Rp",
    "PHP" to "₱",
    "VND" to "₫",
    "TWD" to "NT$",
    "ILS" to "₪",
    "AED" to "د.إ",
    "SAR" to "﷼",
    "ZAR" to "R",
    "MXN" to "MX$",
    "BRL" to "R$",
    "TRY" to "₺",
)

fun String?.toDollar(): String = this.toAbbreviatedTuitionAmount()

fun String?.toAbbreviatedTuitionAmount(): String {
    if(this == null) return "N/A"
    val amount = this.toDoubleOrNull() ?: 0.0
    val result = amount / 1000
    val rounded = (result * 10).roundToInt()
    val wholePart = rounded / 10
    val fractionalPart = rounded % 10
    return "$$wholePart.$fractionalPart"
}

/** Abbreviated tuition (÷1000, one decimal) with the correct currency symbol for [currencyCode]. */
fun formatTuitionFeeAbbreviated(amount: Int, currencyCode: String): String {
    val symbol = iso4217CurrencySymbol(currencyCode)
    val body = amount.toString().toAbbreviatedTuitionAmount()
    return if (symbol.isEmpty()) body else symbol + body
}

fun String.toShortDate(): String {
    val parts = this.trim().split(" ")
    if (parts.size < 2) return this

    val month = parts[0].lowercase()
    val year = parts[1]

    val shortMonth = when (month) {
        "january" -> "Jan"
        "february" -> "Feb"
        "march" -> "Mar"
        "april" -> "Apr"
        "may" -> "May"
        "june" -> "Jun"
        "july" -> "Jul"
        "august" -> "Aug"
        "september" -> "Sep"
        "october" -> "Oct"
        "november" -> "Nov"
        "december" -> "Dec"
        else -> month.take(3).replaceFirstChar { it.uppercase() }
    }

    val shortYear = if (year.length >= 2) year.takeLast(2) else year

    return "$shortMonth $shortYear"
}

fun String.toMonthNumber(): Int? {
    return when (this.trim().lowercase()) {
        "january" -> 1
        "february" -> 2
        "march" -> 3
        "april" -> 4
        "may" -> 5
        "june" -> 6
        "july" -> 7
        "august" -> 8
        "september" -> 9
        "october" -> 10
        "november" -> 11
        "december" -> 12
        else -> null
    }
}

fun Boolean?.orFalse() = this ?: false
fun Double?.toStringOrEmpty(): String = this?.toString() ?: ""

inline fun <reified T> T.toNavJson(): String = Json.encodeToString(this)
inline fun <reified T> String.fromNavJson(): T = Json.decodeFromString(this)