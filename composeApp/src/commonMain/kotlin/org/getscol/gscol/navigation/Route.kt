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
    object Registration : Route

    @Serializable
    object OtpVerification : Route

    @Serializable
    object ForgotPassword : Route

//    @Serializable
//    data class SettingsRoute(val userId: Int) : Route


    @Serializable
    object Splash : Route
}