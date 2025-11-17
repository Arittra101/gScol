package org.getscol.gscol

import androidx.compose.ui.window.ComposeUIViewController
import org.getscol.gscol.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    ScolApp()
}