package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController

/*
   ---------
   In future we will adopt nav3 if nav3 is available for cmp/kmp
   BTW IT WORKS :)
   ---------
*/


class Navigator(private val navController: NavController) {

    private val authRoute = mutableListOf<Route>()
    private val startDestinationRoute = Route.HomeRoute

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

    // will be removed soon
    fun navigateToTopLevel(route: Route) {
        navController.navigate(route) {
            popUpTo(Route.Login) {
                inclusive = true
            }
            // Avoid multiple copies
            launchSingleTop = true

            // Restore state when reselection
            restoreState = true

        }
    }

    // From now try to use this method if possible
    fun navigateTo(route: Route, popUpToStartDestinationRoute: Boolean = false) {
        navController.navigate(route) {
            if (popUpToStartDestinationRoute) {
                popUpTo(startDestinationRoute) { inclusive = false }
                // Avoid multiple copies
                launchSingleTop = true

                // Restore state when reselection
                restoreState = true
            }

        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }



    /* below condition will be removed during refactoring time */
     fun navigateToOtherScreen(route: Route?, dropScreen: Boolean? = null) {
         val targetRoute = route
         if (targetRoute == null) return

         navController.navigate(targetRoute) {
             if (dropScreen == true) {
                 val currentScreen = navController.currentBackStackEntry?.destination?.id ?: return@navigate
                 popUpTo(currentScreen) { inclusive = true }
             }
             else {
                 authRoute.firstOrNull()?.let { popUpTo(Route.HomeRoute) { inclusive = false } }
                 authRoute.clear()
             }
        }

     }

    // will be removed soon
    fun navigateToAuthScreen(loginRoute: Route) {
        authRoute.add(loginRoute)
        navController.navigate(loginRoute)
    }

    // will be removed soon
    fun navigateAuthScreenBack(route: Route){
        authRoute.remove(route)
        navController.popBackStack()
    }

    fun navigateTo(action: NavigationAction) {
        when (action) {
//            NavigationAction.NavigateToHomeScreen -> navigateToTopLevel(TopLevelDestination.HOME)
            NavigationAction.NavigateToCompareScreen -> navigateToTopLevel(TopLevelDestination.COMPARE)
            NavigationAction.NavigateToLogInScreen -> navigateToAuthScreen(Route.Login)
            NavigationAction.NavigateToOtpVerificationScreen -> navigateToOtherScreen(Route.OtpVerification())
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