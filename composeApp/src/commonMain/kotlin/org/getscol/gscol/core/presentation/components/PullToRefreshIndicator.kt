package org.getscol.gscol.core.presentation.components


import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt

@Composable
fun PullToRefreshIndicator(
    refreshState: androidx.compose.material3.pulltorefresh.PullToRefreshState,
    isRefreshing: Boolean
) {
    val rawFraction = refreshState.distanceFraction
    val clampedFraction = rawFraction.coerceIn(0f, 1f)
    val isThresholdCrossed = rawFraction >= 1f
    val rubberFraction = sqrt(rawFraction.coerceAtLeast(0f))

    var containerHeightPx by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    val arrowRotation by animateFloatAsState(
        targetValue = if (isThresholdCrossed) 180f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "ptr_arrow_rotation"
    )

    val scale by animateFloatAsState(
        targetValue = if (isRefreshing) 1f else (0.6f + 0.4f * clampedFraction),
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "ptr_scale"
    )

    val scrimAlpha by animateFloatAsState(
        targetValue = if (isRefreshing) 0.25f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ptr_scrim_alpha"
    )

    // Memoize the calculation to avoid recalculation on every frame
    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isRefreshing) {
            with(density) { (containerHeightPx / 2f).toDp().value - 20f }
        } else {
            -48f + 56f * rubberFraction
        },
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ptr_offset"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(color = Color.Black.copy(alpha = scrimAlpha))
            }
            .onSizeChanged { containerHeightPx = it.height }
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(40.dp)
                .graphicsLayer {
                    translationY = animatedOffsetY.dp.toPx()
                    scaleX = scale
                    scaleY = scale
                    alpha = (clampedFraction / 0.4f).coerceIn(0f, 1f)
                }
                .shadow(elevation = 6.dp, shape = CircleShape, clip = false)
                .clip(CircleShape)
                .background(Color(0xFF8B3838)),
            contentAlignment = Alignment.Center
        ) {
            Crossfade(
                targetState = isRefreshing,
                label = "ptr_content_crossfade"
            ) { refreshing ->
                if (refreshing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Rounded.ArrowDownward,
                        contentDescription = if (isThresholdCrossed) "Release to refresh"
                        else "Pull to refresh",
                        modifier = Modifier
                            .size(20.dp)
                            .graphicsLayer { rotationZ = arrowRotation },
                        tint = Color.White
                    )
                }
            }
        }
    }
}