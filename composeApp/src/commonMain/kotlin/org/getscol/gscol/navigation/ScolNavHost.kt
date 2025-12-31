package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import org.getscol.gscol.App
import org.getscol.gscol.App2
import org.getscol.gscol.auth.presentation.forgotpassword.ForgotPasswordScreen
import org.getscol.gscol.auth.presentation.forgotpassword.ForgotPasswordScreenRoot
import org.getscol.gscol.auth.presentation.login.LoginScreenRoot
import org.getscol.gscol.auth.presentation.otp.OtpVerificationScreenRoot
import org.getscol.gscol.auth.presentation.registration.RegistrationScreenRoot
import org.getscol.gscol.auth.presentation.resetpassword.ResetPasswordRoute
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
        composableNoAnimation<Route.Login> {
            LoginScreenRoot(
                onNavigateToRegistration = {
                    navController.navigate(Route.Registration)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Route.ForgotPassword)
                }
            )
        }
        composableNoAnimation<Route.Registration> {
            RegistrationScreenRoot(
                onNavigateToLogin = {
                    navigator.navigateBack()
                },
                onNavigateToOtpVerification = {
                    navController.navigate(Route.OtpVerification) {
                        popUpTo(Route.Registration) { inclusive = true }
                    }
                }
            )
        }
        composableNoAnimation<Route.ForgotPassword> {
            ForgotPasswordScreenRoot(
                onNavigateToOtpVerification = {
                    navController.navigate(Route.OtpVerification) {
                        popUpTo(Route.ForgotPassword) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composableNoAnimation<Route.OtpVerification> {
            OtpVerificationScreenRoot(
                onVerificationSuccess = {
                    // Navigate to home screen after successful verification
                    navController.navigate(Route.HomeRoute) {
                        popUpTo(Route.OtpVerification) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}