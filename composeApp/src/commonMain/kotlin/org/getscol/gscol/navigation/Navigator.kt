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

    var authRouteList: MutableList<Route> = mutableListOf()

    /*
       popUpTo = remove from the backstack and NavBackStackEntry
       for top level sc bar there NavBackStackEntry is saved in cache
       if NavBackStackEntry is removed then all the instance like viewmodel will be removed
     */

     fun navigateToTopLevel(destination: TopLevelDestination) {
        navController.navigate(destination.route) {
            popUpTo(Route.HomeRoute) {
                saveState = true
                inclusive = false
                println("Here is the " + destination.route)
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

    fun navigateToAuthScreen(route: Route){
        authRouteList.add(route)
        navController.navigate(route)
    }

    /*
       only use when we need to navigate  auth screen to any desire screen
    */
    fun navigateAuthToDesireScreen(route: Route){
        val authFirstRoute = authRouteList.getOrNull(0)
        if(authFirstRoute == null){
            return
        }

        navController.navigate(route){
            popUpTo(authFirstRoute){
                inclusive = true
            }
        }
        authRouteList.clear()
    }

    fun navigateBack() {
        navController.popBackStack()
    }

    fun navigateTo(action: NavigationAction) {
        when (action) {
            NavigationAction.NavigateToHomeScreen -> navigateToTopLevel(TopLevelDestination.HOME)
            NavigationAction.NavigateToCompareScreen -> navigateToTopLevel(TopLevelDestination.COMPARE)
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