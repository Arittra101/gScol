package org.getscol.gscol.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.navigation.TopLevelDestination

@Composable
fun ScolBottomBar(
    destinations: List<TopLevelDestination>,
    currentRoute: String?,
    onNavigateDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()

    // Define colors based on theme
    val backgroundColor = if (isDarkMode) {
        Color(0xFF1C1C1E).copy(alpha = 0.85f) // Dark background
    } else {
        Color.White.copy(alpha = 0.85f) // Light background
    }

    val borderColor = if (isDarkMode) {
        Color.White.copy(alpha = 0.1f)
    } else {
        Color.Black.copy(alpha = 0.08f)
    }

    val unselectedColor = if (isDarkMode) {
        Color(0xFFADB5BD) // Lighter gray for dark mode
    } else {
        Color(0xFF506680) // Original color for light mode
    }

    Box(modifier = modifier) {
        // Blurred background layer with border
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(backgroundColor)
                .blur(radius = 15.dp)
                .drawBehind {
                    // Add subtle top border
                    drawLine(
                        color = borderColor,
                        start = androidx.compose.ui.geometry.Offset(0f, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width, 0f),
                        strokeWidth = 1f
                    )
                }
        )

        // Sharp content layer on top
        NavigationBar(
            windowInsets = WindowInsets(0, 0, 0, 20),
            containerColor = Color.Transparent,
            contentColor = unselectedColor
        ) {
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
                            AsyncImage(
                                model = destination.getIconUri(),
                                contentDescription = destination.label,
                                modifier = Modifier.size(24.dp),
                                colorFilter = ColorFilter.tint(
                                    if (isSelected) Color(0xFFB71C1C) else unselectedColor
                                )
                            )
                            Spacer(modifier = Modifier.height(1.dp))
                            Text(
                                text = destination.label,
                                fontSize = 11.sp,
                                color = if (isSelected) Color(0xFFB71C1C) else unselectedColor
                            )
                        }
                    },
                    label = null,
                    alwaysShowLabel = true,
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color.Transparent,
                        selectedIconColor = Color(0xFFB71C1C),
                        selectedTextColor = Color(0xFFB71C1C),
                        unselectedIconColor = unselectedColor,
                        unselectedTextColor = unselectedColor
                    )
                )
            }
        }
    }
}