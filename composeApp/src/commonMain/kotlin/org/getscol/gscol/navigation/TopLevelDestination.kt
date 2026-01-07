package org.getscol.gscol.navigation

import org.jetbrains.compose.resources.ExperimentalResourceApi
import scol.composeapp.generated.resources.Res

enum class TopLevelDestination(
    val route: Any,
    val icon: String,
    val label: String
) {

    HOME(
        route = Route.HomeRoute,
        icon = "files/ic_home.svg",
        label = "Home"
    ),
    COMPARE(
        route = Route.CompareRoute,
        icon = "files/ic_fav.svg",
        label = "Compare"
    ),

    APPLICATION(
        route = Route.Application,
        icon = "files/ic_com.svg",
        label = "Application"
    ),

    CONSULTANT(
        route = Route.Consultant,
        icon = "files/ic_consultant.svg",
        label = "Consultant"
    ),

    PROFILE(
        route = Route.Profile,
        icon = "files/ic_profile.svg",
        label = "Profile"
    );

    @OptIn(ExperimentalResourceApi::class)
    fun getIconUri(): String = Res.getUri(icon)
}