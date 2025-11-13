package org.getscol.gscol.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home

enum class TopLevelDestination(
    val route: Any,
    val icon: ImageVector,
    val label: String
) {

    HOME(
        route = Route.HomeRoute,
        icon = Icons.Default.Home,
        label = "Home"
    ),
    COMPARE(
        route = Route.CompareRoute,
        icon = Icons.Default.Home,
        label = "Compare"
    ),

//    APPLICATION(
//        route = Route.Application,
//        icon = Icons.Default.Home,
//        label = "Application"
//    ),
//
//    CONSULTANT(
//        route = Route.Consultant,
//        icon = Icons.Default.Home,
//        label = "Consultant"
//    ),
//
//    PROFILE(
//        route = Route.Profile,
//        icon = Icons.Default.Home,
//        label = "Profile"
//    ),

}