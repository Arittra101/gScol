package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController

/*
   ---------
   In future we will adopt nav3 if nav3 is available for cmp/kmp
   File name will be ScolAppState :)
   ---------
*/


class Navigator(private val navController: NavController) {

    fun navigateToTopLevel(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(Route.HomeRoute) {
                saveState = true
                inclusive = false
            }
            // Avoid multiple copies
            launchSingleTop = true

            // Restore state when reselection
            restoreState = true

        }
    }

    fun navigateToOtherScreen(route: Route) {
        navController.navigate(route)
    }

    /*
     popUpTo(0) means pop (remove) all destinations until it reaches the start of the stack (index 0).
     inclusive = true ensures that the very first destination (index 0) is also removed.
     Result: The entire back stack is cleared.
    */

    private fun backStacksClearNavigation(route: Route) {
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    private fun customiseBackStackClearNavigation(route: Route, clearRoute: Route) {
        navController.navigate(route) {
            popUpTo(clearRoute) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateTo(action: NavigationAction) {
        when (action) {
            is NavigationAction.NavigateToHomeScreen -> navigateToTopLevel(TopLevelDestination.HOME)
            is NavigationAction.NavigateToCompareScreen -> navigateToTopLevel(TopLevelDestination.COMPARE)
            is NavigationAction.NavigateToLogInScreen -> backStacksClearNavigation(Route.OtpVerification)
            is NavigationAction.SuccessFullLogInNavigation -> customiseBackStackClearNavigation(action.destinationRoute,action.clearRoute)

            else -> backStacksClearNavigation(Route.Login)
        }
    }
}

@Composable
fun rememberNavigator(navController: NavController): Navigator {
    return remember(navController) {
        Navigator(navController)
    }
}