package org.getscol.gscol

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.example.scol_chuker.CmpChucker
import org.getscol.gscol.core.di.initKoin
import org.getscol.gscol.navigation.LogoutEventManager
import org.getscol.gscol.navigation.NavigationAction
import org.getscol.gscol.theme.ScolTheme

fun MainViewController() = ComposeUIViewController {

    val chuckerModule = CmpChucker.getChuckerModules()
    initKoin(chuckerModule)

    val scope = rememberCoroutineScope()
    val logoutEvent = remember { MutableStateFlow<NavigationAction?>(null) }

    remember {
        scope.launch {
            LogoutEventManager.logoutEvent.collect {
                logoutEvent.value = it
            }
        }
    }
    val currentLogoutEvent by logoutEvent.collectAsState()
    ScolTheme {
        ScolApp(
            currentLogoutEvent = currentLogoutEvent,
            onLogoutHandler = { logoutEvent.value = null }
        )
    }
}