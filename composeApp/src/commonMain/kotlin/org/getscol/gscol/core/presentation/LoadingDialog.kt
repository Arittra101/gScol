package org.getscol.gscol.core.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.getscol.gscol.theme.appColors

private val Brand = Color(0xFF8B3838)

@Composable
fun LoadingDialog(
    message: String = "Please wait",
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        // ✅ Single transition drives ALL animations — no separate transitions per element
        val transition = rememberInfiniteTransition(label = "loading_dialog")

        // Ripple ring — scale 0.6 → 1.6, alpha 0.4 → 0
        val rippleScale by transition.animateFloat(
            initialValue = 0.6f, targetValue = 1.6f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "ripple_scale"
        )
        val rippleAlpha by transition.animateFloat(
            initialValue = 0.35f, targetValue = 0f,
            animationSpec = infiniteRepeatable(
                animation = tween(1400, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart
            ), label = "ripple_alpha"
        )

        // Three dots — staggered bounce via different delayMillis
        val dot1 by transition.animateFloat(
            initialValue = 0f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = 0, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "dot1"
        )
        val dot2 by transition.animateFloat(
            initialValue = 0f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = 150, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "dot2"
        )
        val dot3 by transition.animateFloat(
            initialValue = 0f, targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(600, delayMillis = 300, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "dot3"
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = appColors().customBackground,
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(vertical = 40.dp, horizontal = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // ── Icon + ripple ──────────────────────────────────────────
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(72.dp)
                ) {
                    // ✅ Ripple drawn entirely in draw phase — no composable node
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .drawBehind {
                                drawCircle(
                                    color = Brand.copy(alpha = rippleAlpha),
                                    radius = size.minDimension / 2f * rippleScale
                                )
                            }
                    )

                    // Solid brand circle with icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(color = Brand, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        // Spinner arc drawn via drawBehind — no CircularProgressIndicator composable
                        // Uses the ripple scale inverted so it spins opposite to the ripple
                        val spinRotation by transition.animateFloat(
                            initialValue = 0f, targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Restart
                            ), label = "spin"
                        )
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .graphicsLayer { rotationZ = spinRotation }
                                .drawBehind {
                                    // Two arcs: a faint track + a bright moving segment
                                    drawArc(
                                        color = Color.White.copy(alpha = 0.25f),
                                        startAngle = 0f,
                                        sweepAngle = 360f,
                                        useCenter = false,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = 3.dp.toPx()
                                        )
                                    )
                                    drawArc(
                                        color = Color.White,
                                        startAngle = -90f,
                                        sweepAngle = 270f,
                                        useCenter = false,
                                        style = androidx.compose.ui.graphics.drawscope.Stroke(
                                            width = 3.dp.toPx(),
                                            cap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                    )
                                }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ── Label ─────────────────────────────────────────────────
                Text(
                    text = message,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1A1A1A),
                    letterSpacing = 0.2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // ── Staggered bouncing dots ────────────────────────────────
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(dot1, dot2, dot3).forEach { progress ->
                        // ✅ translateY and alpha both in graphicsLayer — draw phase only
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .graphicsLayer {
                                    translationY = -8.dp.toPx() * progress
                                    alpha = 0.4f + 0.6f * progress
                                    scaleX = 0.8f + 0.2f * progress
                                    scaleY = 0.8f + 0.2f * progress
                                }
                                .background(color = Brand, shape = CircleShape)
                        )
                    }
                }
            }
        }
    }
}