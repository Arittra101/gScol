package org.getscol.gscol.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import org.getscol.gscol.App
import org.getscol.gscol.App2

@Composable
fun ScolNavHost(
    navController: NavHostController,
    navigator: Navigator,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Route.HomeRoute,
    ){
       composable<Route.HomeRoute>{
           App()
       }
        composable<Route.CompareRoute>{
            App2()
        }
    }
}