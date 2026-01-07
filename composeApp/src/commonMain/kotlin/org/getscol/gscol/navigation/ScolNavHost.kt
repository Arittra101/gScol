package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.getscol.gscol.App
import org.getscol.gscol.App2
import org.getscol.gscol.DesireScreen
import org.getscol.gscol.auth.presentation.forgotpassword.ForgotPasswordScreenRoot
import org.getscol.gscol.auth.presentation.login.LoginScreenRoot
import org.getscol.gscol.auth.presentation.otp.OtpVerificationScreenRoot
import org.getscol.gscol.auth.presentation.registration.RegistrationScreenRoot
import org.getscol.gscol.auth.presentation.resetpassword.ResetPasswordRoute
import org.getscol.gscol.core.Helper.composableNoAnimation
import org.getscol.gscol.splash.SplashScreen

@Composable
fun ScolNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.Splash,
    ) {
        composableNoAnimation<Route.Splash> {
            SplashScreen(
                onNavigateToHome = {
                    navController.navigate(Route.HomeRoute) {
                        popUpTo(Route.Splash) { inclusive = true }
                    }
                }
            )
        }

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
        composableNoAnimation<Route.Desire> {
            DesireScreen()
        }
        composableNoAnimation<Route.Login> {
            LoginScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.SignUp> {
            RegistrationScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.ForgotPassword> {
            ForgotPasswordScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.OtpVerification> {
            OtpVerificationScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.ResetPassword> {
            ResetPasswordRoute(navigator = navigator)
        }
    }
}