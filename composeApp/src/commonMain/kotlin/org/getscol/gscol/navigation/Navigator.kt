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

    private fun navigateToOtherScreen(route: Route) {
        navController.navigate(route)
    }

    private fun navigateToLogIn(route: Route) {
        navController.navigate(route) {
            popUpTo(0) { inclusive = true }
            launchSingleTop = true
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateTo(action: NavigationAction) {
        when (action) {
            NavigationAction.NavigateToHomeScreen -> navigateToTopLevel(TopLevelDestination.HOME)
            NavigationAction.NavigateToCompareScreen -> navigateToTopLevel(TopLevelDestination.COMPARE)
            NavigationAction.NavigateToLogInScreen -> navigateToLogIn(Route.Login)
            NavigationAction.NavigateToOtpVerificationScreen -> navigateToOtherScreen(Route.OtpVerification)
            else -> navigateToTopLevel(TopLevelDestination.COMPARE)
        }
    }
}

@Composable
fun rememberNavigator(navController: NavController): Navigator {
    return remember(navController) {
        Navigator(navController)
    }
}