package org.getscol.gscol.navigation

sealed interface NavigationAction {
    data object NavigateToLogInScreen : NavigationAction
}

