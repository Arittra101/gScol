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

//    @Serializable
//    data class SettingsRoute(val userId: Int) : Route

}