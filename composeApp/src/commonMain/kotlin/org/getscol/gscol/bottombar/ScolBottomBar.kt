package org.getscol.gscol.bottombar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.getscol.gscol.navigation.TopLevelDestination

@Composable
fun ScolBottomBar(
    destinations: List<TopLevelDestination>,
    currentRoute: String?,
    onNavigateDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        destinations.forEach { destination ->
            val routeClass = destination.route::class.qualifiedName
            val isSelected = currentRoute?.substringBefore("?").equals(routeClass)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateDestination(destination) },
                icon = { Icon(destination.icon, contentDescription = null) },
                label = { Text(destination.label) }
            )
        }
    }
}