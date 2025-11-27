package org.getscol.gscol.navigation

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

object LogoutEventManager {
    private val _logoutEventChannel = Channel<NavigationAction>(Channel.BUFFERED)
    val logoutEvent: Flow<NavigationAction> = _logoutEventChannel.receiveAsFlow()

    fun sendLogoutEvent(navigationAction: NavigationAction) {
        _logoutEventChannel.trySend(navigationAction)
            .onFailure {
                println("Error sending logout event: $it")
            }
    }
}