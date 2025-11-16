package org.getscol.gscol.Helper

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