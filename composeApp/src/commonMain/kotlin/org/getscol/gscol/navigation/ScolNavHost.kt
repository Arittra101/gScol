package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.getscol.gscol.App
import org.getscol.gscol.App2
import org.getscol.gscol.auth.presentation.login.LoginScreenRoot
import org.getscol.gscol.auth.presentation.verfication.OtpVerificationScreenRoot
import org.getscol.gscol.core.Helper.composableNoAnimation

@Composable
fun ScolNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Login,
    ) {
        composableNoAnimation<Route.HomeRoute> {
            App(navigator)
        }
        composableNoAnimation<Route.CompareRoute> {
            App2()
        }
        composableNoAnimation<Route.Application> {
            App(navigator)
        }
        composableNoAnimation<Route.Profile> {
            App2()
        }
        composableNoAnimation<Route.Consultant> {
            App(navigator)
        }
        composableNoAnimation<Route.OtpVerification> {
            OtpVerificationScreenRoot(navigator)
        }
        composableNoAnimation<Route.Login> {
            LoginScreenRoot(navigator)
        }
    }
}