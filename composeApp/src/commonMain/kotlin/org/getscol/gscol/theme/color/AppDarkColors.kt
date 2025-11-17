package org.getscol.gscol.theme.color

import androidx.compose.ui.graphics.Color

fun appDarkColors(): AppColors = AppColors(
    // Reuse LightAppColors class to hold values; values are dark-tuned
    primary = Color(0xFF0057D9),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF003A95),
    onPrimaryContainer = Color(0xFFD6E2FF),
    secondary = Color(0xFF556380),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF394659),
    onSecondaryContainer = Color(0xFFD9E2FF),
    tertiary = Color(0xFF7A5EA7),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF57437A),
    onTertiaryContainer = Color(0xFFEBDCFF),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF101418),
    onBackground = Color(0xFFE1E2E6),
    surface = Color(0xFF121417),
    onSurface = Color(0xFFE2E2E6),
    outline = Color(0xFF8D9099),
    // Custom
    customPrimary = Color(0xFF80A9FF),
    customSecondary = Color(0xFF9AA7C2),
    customPrimaryText = Color(0xFF0B1220),
    customSecondaryText = Color(0xFF111827),
    customError = Color(0xFFFFB4AB),
    customErrorText = Color(0xFF370001),
    customSuccess = Color(0xFF52C41A),
    customSuccessText = Color(0xFF0C1406),
    customWarning = Color(0xFFFFB74D),
    customInfo = Color(0xFF4FC3F7)
)


