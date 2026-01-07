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

    enum class AuthRoute {
        LOGIN,
        REGISTRATION,
        FORGOT_PASSWORD,
        OTP_VERIFICATION
    }

    private val authRoute = mutableListOf<Route>()

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

    /* for reset password support & login screen support */
     fun navigateToOtherScreen(route: Route?, homeScreen: Boolean? = null, dropScreen: Boolean? = null) {
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

    fun navigateToAuthScreen(loginRoute: Route) {
        authRoute.add(loginRoute)
        navController.navigate(loginRoute)
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateAuthScreenBack(route: Route){
        authRoute.remove(route)
        navController.popBackStack()
    }

    fun navigateTo(action: NavigationAction) {
        when (action) {
            NavigationAction.NavigateToHomeScreen -> navigateToTopLevel(TopLevelDestination.HOME)
            NavigationAction.NavigateToCompareScreen -> navigateToTopLevel(TopLevelDestination.COMPARE)
            NavigationAction.NavigateToLogInScreen -> navigateToAuthScreen(Route.Login)
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