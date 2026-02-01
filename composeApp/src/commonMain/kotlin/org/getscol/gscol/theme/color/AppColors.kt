package org.getscol.gscol.theme.color

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalAppColors = compositionLocalOf<AppColors> {
    error("No AppColors provided. Make sure your composables are inside ScolTheme.")
}

data class AppColors(
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val tertiaryContainer: Color,
    val onTertiaryContainer: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val outline: Color,
    // Custom
    val customPrimary: Color,
    val customSecondary: Color,
    val customPrimaryText: Color,
    val customSecondaryText: Color,
    val customBackground: Color,
    val customPrimaryContainer: Color,
    val customSecondaryContainer: Color,
    val customError: Color,
    val customErrorText: Color,
    val customSuccess: Color,
    val customSuccessText: Color,
    val customWarning: Color,
    val customInfo: Color,
)