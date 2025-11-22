package org.getscol.gscol

import androidx.compose.ui.window.ComposeUIViewController
import org.getscol.gscol.core.di.initKoin
import org.getscol.gscol.theme.ScolTheme

fun MainViewController() = ComposeUIViewController {
    initKoin()
    ScolTheme {
        ScolApp()
    }
}