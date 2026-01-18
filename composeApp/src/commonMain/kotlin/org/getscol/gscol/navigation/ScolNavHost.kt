package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import org.getscol.gscol.App
import org.getscol.gscol.DesireScreen
import org.getscol.gscol.auth.presentation.application.ApplicationScreen
import org.getscol.gscol.auth.presentation.compare.CompareScreen
import org.getscol.gscol.auth.presentation.consultant.ConsultantScreen
import org.getscol.gscol.auth.presentation.profile.ProfileScreen
import org.getscol.gscol.auth.presentation.splash.SplashScreen
import org.getscol.gscol.core.Helper.composableNoAnimation
import org.getscol.gscol.feature.auth.presentation.forgotpassword.ForgotPasswordScreenRoot
import org.getscol.gscol.feature.auth.presentation.login.LoginScreenRoot
import org.getscol.gscol.feature.auth.presentation.otp.OtpVerificationScreenRoot
import org.getscol.gscol.feature.auth.presentation.registration.RegistrationScreenRoot
import org.getscol.gscol.feature.auth.presentation.resetpassword.ResetPasswordRoute

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
            CompareScreen()
        }
        composableNoAnimation<Route.Application> {
            ApplicationScreen()
        }
        composableNoAnimation<Route.Profile> {
            ProfileScreen()
        }
        composableNoAnimation<Route.Consultant> {
            ConsultantScreen()
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
        composableNoAnimation<Route.OtpVerification> {backStackEntry->
            val args = backStackEntry.toRoute<Route.OtpVerification>()
            OtpVerificationScreenRoot(navigator = navigator, otpNumber = args.otp.orEmpty())
        }
        composableNoAnimation<Route.ResetPassword> {
            ResetPasswordRoute(navigator = navigator)
        }
    }
}