package org.getscol.gscol.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.getscol.gscol.theme.color.LocalAppColors
import org.getscol.gscol.theme.color.appLightColors
import org.getscol.gscol.theme.scheme.scolLightColorScheme

@Composable
fun ScolTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {

    /*
         For now we don't support dark mood
         val colorScheme = if (isDarkTheme) scolDarkColorScheme() else scolLightColorScheme()
         val appColors = if (isDarkTheme) appDarkColors() else appLightColors()
     */

    val colorScheme = if (isDarkTheme) scolLightColorScheme() else scolLightColorScheme()
    val appColors = if (isDarkTheme) appLightColors() else appLightColors()

    CompositionLocalProvider(LocalAppColors provides appColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ScolTypography,
            shapes = ScolShapes,
            content = content
        )
    }
}


