package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import kotlinx.serialization.json.Json
import org.getscol.gscol.DesireScreen
import org.getscol.gscol.feature.application.ApplicationScreen
import org.getscol.gscol.auth.presentation.compare.CompareScreen
import org.getscol.gscol.auth.presentation.consultant.ConsultantScreen
import org.getscol.gscol.auth.presentation.profile.ProfileScreenRoute
import org.getscol.gscol.auth.presentation.splash.SplashScreen
import org.getscol.gscol.core.helper.composableNoAnimation
import org.getscol.gscol.feature.academic_form.presentation.AcademicFormRoute
import org.getscol.gscol.feature.common_media.presentation.InAppWebViewScreenRoot
import org.getscol.gscol.feature.course_details.presentation.course_details.CourseDetailsScreenRoot
import org.getscol.gscol.feature.auth.presentation.forgotpassword.ForgotPasswordScreenRoot
import org.getscol.gscol.feature.auth.presentation.login.LoginScreenRoot
import org.getscol.gscol.feature.auth.presentation.otp.OtpVerificationScreenRoot
import org.getscol.gscol.feature.auth.presentation.registration.RegistrationScreenRoot
import org.getscol.gscol.feature.auth.presentation.resetpassword.ResetPasswordRoute
import org.getscol.gscol.feature.home.presentation.HomeScreenRoot
import org.getscol.gscol.feature.home.presentation.InEligibleScreenRoute
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.presentation.advance_search.AdvancedSearchScreenRoot
import org.getscol.gscol.feature.search.presentation.search.SearchScreenRoot
import org.getscol.gscol.feature.search.presentation.search_result.SearchResultsScreenRoot

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
            HomeScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.Search> {
            SearchScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.SearchResults> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.SearchResults>()
            val advancedParams = args.advancedParamsJson?.let { json ->
                try {
                    Json.decodeFromString<AdvancedSearchParams>(json)
                } catch (_: Exception) {
                    null
                }
            }
            SearchResultsScreenRoot(
                searchText = args.searchText,
                listType = args.listType,
                advancedParams = advancedParams,
                navigator = navigator
            )
        }
        composableNoAnimation<Route.AdvancedSearch> {
            AdvancedSearchScreenRoot(navigator = navigator)
        }
        composableNoAnimation<Route.CompareRoute> {
            CompareScreen()
        }
        composableNoAnimation<Route.Application> {
            ApplicationScreen()
        }
        composableNoAnimation<Route.Profile> {
            ProfileScreenRoute(navigator = navigator)
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
        composableNoAnimation<Route.OtpVerification> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.OtpVerification>()
            OtpVerificationScreenRoot(navigator = navigator, otpNumber = args.otp.orEmpty())
        }
        composableNoAnimation<Route.ResetPassword> {
            ResetPasswordRoute(navigator = navigator)
        }
        composableNoAnimation<Route.AcademicForm> {
            AcademicFormRoute(navigator)
        }
        composableNoAnimation<Route.CourseDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.CourseDetails>()
            CourseDetailsScreenRoot(courseId = args.courseId, navigator = navigator)
        }
        composableNoAnimation<Route.CourseVideoPlayer> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.CourseVideoPlayer>()
            InAppWebViewScreenRoot(title = args.title, url = args.videoUrl, navigator = navigator)
        }
        composableNoAnimation<Route.WebViewRoute> { backStackEntry ->
            val args = backStackEntry.toRoute<Route.WebViewRoute>()
            InAppWebViewScreenRoot(title = args.title, url = args.url, navigator = navigator)
        }
        composableNoAnimation<Route.InEligibleScreen> {
            InEligibleScreenRoute(navigator = navigator)
        }
        composableNoAnimation<Route.InEligibleScreen> {
            InEligibleScreenRoute(navigator = navigator)
        }
    }
}