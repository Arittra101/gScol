package org.getscol.gscol.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import org.getscol.gscol.theme.scheme.scolDarkColorScheme
import org.getscol.gscol.theme.scheme.scolLightColorScheme

@Composable
fun ScolTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkTheme) scolDarkColorScheme() else scolLightColorScheme()

    MaterialTheme(
        colorScheme = colorScheme,
        typography = ScolTypography,
        shapes = ScolShapes,
        content = content
    )

}


