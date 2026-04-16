package org.getscol.gscol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.core.utils.setContext
import org.getscol.gscol.navigation.LogoutEventManager
import org.getscol.gscol.navigation.NavigationAction
import org.getscol.gscol.theme.ScolTheme


class MainActivity : ComponentActivity() {

    private val _logoutEvent = MutableStateFlow<NavigationAction?>(null)
    private val logoutEvent: StateFlow<NavigationAction?> = _logoutEvent.asStateFlow()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContext(this)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                LogoutEventManager.logoutEvent.collect { it ->
                    _logoutEvent.value = it
                }
            }
        }

        //forcefully set the system light mood for my app
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true  // dark icons on light background
            isAppearanceLightNavigationBars = true
        }

        setContent {
            val currentLogoutEvent by logoutEvent.collectAsState()
            ScolTheme {
                ScolApp(
                    currentLogoutEvent= currentLogoutEvent,
                    onLogoutHandler = { _logoutEvent.value = null }
                )
            }
        }
    }
}