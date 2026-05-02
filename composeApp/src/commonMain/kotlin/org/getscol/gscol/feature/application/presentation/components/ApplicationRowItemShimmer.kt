package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ApplicationRowItemShimmer(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.4.dp, Color(0xFFEEEEEE))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Logo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {

                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Subtitle
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Status badge
            Box(
                modifier = Modifier
                    .height(24.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(50))
                    .shimmerEffect()
            )
        }
    }
}



fun Modifier.shimmerEffect(): Modifier = composed {

    val transition = rememberInfiniteTransition(label = "shimmer")

    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerAnim"
    )

    val shimmerColors = listOf(
        Color(0xFF8B3838).copy(alpha = 0.15f),
        Color(0xFF8B3838).copy(alpha = 0.05f),
        Color(0xFF8B3838).copy(alpha = 0.15f)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim.value - 200f, 0f),
        end = Offset(translateAnim.value, 0f)
    )

    this.background(brush)
}