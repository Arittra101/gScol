package org.getscol.gscol

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.navigation.LogoutEventManager
import org.getscol.gscol.navigation.NavigationAction

class AppViewmodel : ViewModel() {

    private val _loginEvent = MutableStateFlow<NavigationAction?>(null)
    val loginEvent: StateFlow<NavigationAction?> = _loginEvent.asStateFlow()

    init {
        viewModelScope.launch {
            LogoutEventManager.logoutEvent.collect {
                _loginEvent.value = it
            }
        }
    }

    fun onLogoutHandler() = { _loginEvent.value = null }


}