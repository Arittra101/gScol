package org.getscol.gscol.navigation

sealed interface NavigationAction {
    data object NavigateToHomeScreen : NavigationAction
    data object NavigateToCompareScreen : NavigationAction
    data object NavigateToConsultantScreen : NavigationAction
    data object NavigateToProfileScreen : NavigationAction
    data object NavigateToApplicationScreen : NavigationAction
}

