package org.getscol.gscol

import androidx.compose.runtime.Composable
import org.getscol.gscol.auth.presentation.splash.SplashScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun LoginScreenPreview() {
    SplashScreen(onNavigateToHome = {})
}
