package org.getscol.gscol.bottombar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import org.getscol.gscol.theme.appColors

@Composable
fun ScolBottomBar(
    destinations: List<TopLevelDestination>,
    currentRoute: String?,
    onNavigateDestination: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkMode = isSystemInDarkTheme()

    // For now we don't support dark mood
   /* val backgroundColor = if (isDarkMode) {
        Color(0xFF1C1C1E).copy(alpha = 0.85f) // Dark background
    } else {
        Color.White.copy(alpha = 0.85f) // Light background
    }*/

    // Define colors based on theme ~ for now we don't support dark mood
    val backgroundColor = Color.White.copy(alpha = 0.85f)

    val borderColor = if (isDarkMode) {
//        Color.White.copy(alpha = 0.1f)
        Color.Black.copy(alpha = 0.08f)
    } else {
        Color.Black.copy(alpha = 0.08f)
    }

    val unselectedColor = if (isDarkMode) {
        appColors().customSecondaryText // Lighter gray for dark mode
    } else {
        appColors().customSecondaryText // Original color for light mode
    }

    Box(modifier = modifier.windowInsetsPadding(WindowInsets.navigationBars)) {
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
                        strokeWidth = 2f
                    )
                }
                .windowInsetsPadding(WindowInsets.navigationBars)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            destinations.forEach { destination ->
                val routeClass = destination.route::class.qualifiedName
                val isSelected = currentRoute?.substringBefore("?").equals(routeClass)

                val interactionSource = remember { MutableInteractionSource() }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null // No ripple
                        ) {
                            if (!isSelected) {
                                onNavigateDestination(destination)
                            }
                        }
                        .padding(vertical = 4.dp)
                ) {
                    AsyncImage(
                        model = destination.getIconUri(),
                        contentDescription = destination.label,
                        modifier = Modifier.size(24.dp),
                        colorFilter = ColorFilter.tint(
                            if (isSelected) appColors().customPrimary else unselectedColor
                        )
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = destination.label,
                        fontSize = 11.sp,
                        color = if (isSelected) appColors().customPrimary else unselectedColor
                    )
                }
            }
        }
    }
}