package org.getscol.gscol.core.Helper

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
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

fun String.toDollar(): String {
    val amount = this.toDoubleOrNull() ?: 0.0
    val result = amount / 1000

    val rounded = (result * 10).roundToInt()
    val wholePart = rounded / 10
    val fractionalPart = rounded % 10

    return "$$wholePart.$fractionalPart"
}

fun String.toShortDate(): String {
    val parts = this.trim().split(" ")
    if (parts.size < 2) return this

    val month = parts[0].lowercase()
    val year = parts[1]

    val shortMonth = when (month) {
        "january"   -> "Jan"
        "february"  -> "Feb"
        "march"     -> "Mar"
        "april"     -> "Apr"
        "may"       -> "May"
        "june"      -> "Jun"
        "july"      -> "Jul"
        "august"    -> "Aug"
        "september" -> "Sep"
        "october"   -> "Oct"
        "november"  -> "Nov"
        "december"  -> "Dec"
        else        -> month.take(3).replaceFirstChar { it.uppercase() }
    }
    val shortYear = if (year.length >= 2) year.takeLast(2) else year
    return "$shortMonth $shortYear".uppercase()
}