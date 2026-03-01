package org.getscol.gscol.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object HomeRoute : Route

    @Serializable
    object Application : Route

    @Serializable
    object CompareRoute : Route

    @Serializable
    object Consultant : Route

    @Serializable
    object Profile : Route

    @Serializable
    object Login : Route

    @Serializable
    object SignUp : Route

    @Serializable
    data class OtpVerification(val otp: String? = null) : Route

    @Serializable
    object ForgotPassword : Route

    @Serializable
    object ResetPassword : Route

    @Serializable
    object Desire : Route

    @Serializable
    object Search : Route

    @Serializable
    data class SearchResults(
        val searchText: String,
        val listType: String = "ELIGIBLE_ONLY",
        val advancedParamsJson: String? = null
    ) : Route

    @Serializable
    object AdvancedSearch : Route

    @Serializable
    object AcademicForm : Route

//    @Serializable
//    data class SettingsRoute(val userId: Int) : Route


    @Serializable
    object Splash : Route
}