package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.getscol.gscol.App
import org.getscol.gscol.App2
import org.getscol.gscol.auth.presentation.login.LoginScreen
import org.getscol.gscol.core.Helper.composableNoAnimation

@Composable
fun ScolNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeRoute,
    ) {
        composableNoAnimation<Route.HomeRoute> {
            App()
        }
        composableNoAnimation<Route.CompareRoute> {
            App2()
        }
        composableNoAnimation<Route.Application>{
            App()
        }
        composableNoAnimation<Route.Profile> {
            App2()
        }
        composableNoAnimation<Route.Consultant> {
            App()
        }
        composableNoAnimation<Route.Login>{
            LoginScreen()
        }
    }
}