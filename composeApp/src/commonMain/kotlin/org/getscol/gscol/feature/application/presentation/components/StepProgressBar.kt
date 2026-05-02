package org.getscol.gscol.feature.application.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.application.domain.model.response.ProgressItems
import org.getscol.gscol.feature.application.domain.model.response.StageState
import org.getscol.gscol.feature.application.presentation.application_status_tracker.CardBackground
import org.getscol.gscol.feature.application.presentation.application_status_tracker.GrayPending
import org.getscol.gscol.feature.application.presentation.application_status_tracker.GreenActive
import org.getscol.gscol.feature.application.presentation.application_status_tracker.GreenCompleted
import org.getscol.gscol.feature.application.presentation.application_status_tracker.GreyLightInactive

@Composable
fun StepProgressBar(
    steps: List<ProgressItems>,
    progressPercentage: String,
    modifier: Modifier = Modifier,
    stepSize: Dp = 48.dp,
    lineHeight: Dp = 4.dp,
    edgeFadeWidth: Dp = 32.dp,
) {
    //require(steps.size in 1..20) { "Steps must be between 1 and 20" }

    val scrollState = rememberScrollState()

    val itemOffsets = remember { mutableStateMapOf<Int, Int>() }
    val extraScrollOffset = 0

    LaunchedEffect(steps) {
        val activeIndex = steps.indexOfFirst { it.state == StageState.CURRENT }
        if (activeIndex > 2) {
            itemOffsets[activeIndex]?.let { offset ->
                scrollState.animateScrollTo(offset + extraScrollOffset)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardBackground)
            .padding(vertical = 20.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Text(
                text = "${progressPercentage}% completed!",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = GreenCompleted,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            Spacer(Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fadingEdge(edgeFadeWidth, CardBackground)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                        .padding(horizontal = 16.dp)
                ) {
                    steps.forEachIndexed { index, step ->
                        StepCircle(
                            state = step.state ?: StageState.UPCOMING,
                            size = stepSize,
                            modifier = Modifier.onGloballyPositioned { coordinates ->
                                itemOffsets[index] = coordinates.positionInParent().x.toInt()
                            })
                        if (index < steps.lastIndex) {
                            StepConnector(
                                fromState = step.state ?: StageState.UPCOMING,
                                toState = steps[index + 1].state ?: StageState.UPCOMING,
                                height = lineHeight
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepCircle(state: StageState, size: Dp, modifier: Modifier = Modifier) {
    when (state) {
        StageState.COMPLETED -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(GreenCompleted)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Completed",
                    tint = Color.White,
                    modifier = Modifier.size(size * 0.5f)
                )
            }
        }

        StageState.CURRENT -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(size)
                    .drawWithContent {
                        // Draw circles first (behind children)
                        drawCircle(
                            color = GreenActive.copy(alpha = 0.3f),
                            radius = this.size.minDimension / 2f,
                        )
                        drawCircle(
                            color = GreenActive,
                            radius = this.size.minDimension / 3.8f,
                        )
                        // Draw children (inner Box) on top
                        drawContent()
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(size * 0.15f)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        StageState.UPCOMING -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(size)
                    .drawWithContent {
                        // Draw circles first (behind children)
                        drawCircle(
                            color = GreyLightInactive.copy(alpha = 0.3f),
                            radius = this.size.minDimension / 2f,
                        )
                        drawCircle(
                            color = GreyLightInactive,
                            radius = this.size.minDimension / 3.8f,
                        )
                        // Draw children (inner Box) on top
                        drawContent()
                    }
            ) {
                Box(
                    modifier = Modifier
                        .size(size * 0.15f)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }
        }

        StageState.UNKNOWN -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(CircleShape)
                    .background(GrayPending)
            )
        }
    }
}


@Composable
fun StepConnector(
    fromState: StageState,
    toState: StageState,
    height: Dp,
    width: Dp = 40.dp
) {
    val color = when {
        fromState == StageState.COMPLETED && toState == StageState.COMPLETED -> GreenCompleted
        fromState == StageState.COMPLETED && toState == StageState.CURRENT -> GreenCompleted
        else -> GrayPending
    }

    Box(
        modifier = Modifier
            .width(width)
            .height(height)
            .background(color)
    )
}

fun Modifier.fadingEdge(fadeWidth: Dp, background: Color): Modifier =
    this.drawWithContent {
        drawContent()
        // Left fade
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(background, Color.Transparent),
                startX = 0f,
                endX = fadeWidth.toPx()
            )
        )
        // Right fade
        drawRect(
            brush = Brush.horizontalGradient(
                colors = listOf(Color.Transparent, background),
                startX = size.width - fadeWidth.toPx(),
                endX = size.width
            )
        )
    }

