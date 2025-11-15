package org.getscol.gscol.bottombar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.navigation.TopLevelDestination

@Composable
fun ScolBottomBar(
    destinations: List<TopLevelDestination>,
    currentRoute: String?,
    onNavigateDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier,
        windowInsets = WindowInsets(0, 0, 0, 20) ) {
        destinations.forEach { destination ->
            val routeClass = destination.route::class.qualifiedName
            val isSelected = currentRoute?.substringBefore("?").equals(routeClass)

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigateDestination(destination) },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 10.dp)
                    ) {
                        Icon(
                            destination.icon,
                            contentDescription = null,
                            tint = if (isSelected) Color(0xFFB71C1C) else Color(0xFF506680)
                        )
                        Spacer(modifier = Modifier.height(1.dp))
                        Text(
                            text = destination.label,
                            fontSize = 11.sp,
                            color = if (isSelected) Color(0xFFB71C1C) else Color(0xFF506680)
                        )
                    }
                },
                label = null,
                alwaysShowLabel = true,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = Color(0xFFB71C1C),
                    selectedTextColor = Color(0xFFB71C1C),
                    unselectedIconColor = Color(0xFF506680),
                    unselectedTextColor = Color(0xFF506680)
                )

            )
        }
    }
}