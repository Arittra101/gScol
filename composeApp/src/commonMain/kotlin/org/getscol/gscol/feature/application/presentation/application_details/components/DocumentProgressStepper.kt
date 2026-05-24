package org.getscol.gscol.feature.application.presentation.application_details.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.application.presentation.DocumentCategoryState
import org.getscol.gscol.feature.application.presentation.DocumentProgressStep
import org.getscol.gscol.feature.application.presentation.application_details.DocumentTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

private object DocumentProgressTheme {
    val CompleteGreen = Color(0xFF34C759)
    val ConnectorGreen = Color(0xFFB8E6C1)
    val HaloGreen = Color(0xFF34C759)
    val PendingRed = Color(0xFF8B1A2B)
    val PanelBackground = Color(0xFFF2F2F7)
    val VerifiedBorder = Color(0xFFE8E8ED)
}

private val HaloSize = 38.dp
private val InnerCoreSize = 18.dp
private val InnerDotSize = 5.dp
private val VerifiedSize = 28.dp
private val IndicatorRowHeight = HaloSize + 4.dp
private const val VisibleStepsOnScreen = 4
private val DocumentProgressBarHeight = 124.dp

@Composable
fun DocumentProgressStepper(
    steps: List<DocumentProgressStep>,
    modifier: Modifier = Modifier,
) {
    if (steps.isEmpty()) return

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DocumentProgressTheme.PanelBackground)
            .drawBehind {
                drawLine(
                    color = DocumentTheme.CardBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx(),
                )
            }
            .padding(vertical = 14.dp),
    ) {
        Text(
            text = "Document Progress",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = DocumentTheme.TextPrimary,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 14.dp),
        )

        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val stepWidth = maxWidth / VisibleStepsOnScreen
            val trackWidth = stepWidth * steps.size

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
            ) {
                Box(modifier = Modifier.width(trackWidth)) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = IndicatorRowHeight / 2 - 1.dp)
                            .width(trackWidth)
                            .padding(horizontal = stepWidth / 2)
                            .height(2.dp)
                            .background(DocumentProgressTheme.ConnectorGreen),
                    )

                    Row(modifier = Modifier.fillMaxWidth()) {
                        steps.forEach { step ->
                            DocumentProgressStepItem(
                                step = step,
                                stepWidth = stepWidth,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DocumentProgressStepItem(
    step: DocumentProgressStep,
    stepWidth: Dp,
) {
    Column(
        modifier = Modifier.width(stepWidth),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IndicatorRowHeight),
            contentAlignment = Alignment.Center,
        ) {
            DocumentProgressIndicator(status = step.status)
        }

        Text(
            text = step.label,
            fontSize = 11.sp,
            color = DocumentTheme.TextSecondary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            softWrap = false,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, start = 4.dp, end = 4.dp),
        )
    }
}

@Composable
private fun DocumentProgressIndicator(
    status: DocumentCategoryState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.size(HaloSize),
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            DocumentCategoryState.VERIFIED -> VerifiedStepIndicator()
            DocumentCategoryState.IN_PROGRESS -> ActiveStepIndicator(
                coreColor = DocumentProgressTheme.CompleteGreen,
            )
            DocumentCategoryState.PENDING -> ActiveStepIndicator(
                coreColor = DocumentProgressTheme.PendingRed,
            )
        }
    }
}

@Composable
private fun VerifiedStepIndicator() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(VerifiedSize)
            .border(1.5.dp, DocumentProgressTheme.VerifiedBorder, CircleShape)
            .clip(CircleShape)
            .background(DocumentProgressTheme.CompleteGreen),
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Complete",
            tint = Color.White,
            modifier = Modifier.size(15.dp),
        )
    }
}

@Composable
private fun ActiveStepIndicator(coreColor: Color) {
    Box(
        modifier = Modifier
            .size(HaloSize)
            .clip(CircleShape)
            .background(DocumentProgressTheme.HaloGreen.copy(alpha = 0.22f)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(InnerCoreSize)
                .clip(CircleShape)
                .background(coreColor),
        ) {
            Box(
                modifier = Modifier
                    .size(InnerDotSize)
                    .clip(CircleShape)
                    .background(Color.White),
            )
        }
    }
}

fun documentProgressBottomInset(showProgress: Boolean) = if (showProgress) DocumentProgressBarHeight else 0.dp

@Preview
@Composable
private fun DocumentProgressStepperPreview() {
    DocumentProgressStepper(
        steps = listOf(
            DocumentProgressStep("Transcripts", DocumentCategoryState.VERIFIED),
            DocumentProgressStep("SOP", DocumentCategoryState.PENDING),
            DocumentProgressStep("Recommendation Letter", DocumentCategoryState.VERIFIED),
            DocumentProgressStep("Resume", DocumentCategoryState.IN_PROGRESS),
            DocumentProgressStep("Passport", DocumentCategoryState.PENDING),
            DocumentProgressStep("IELTS", DocumentCategoryState.IN_PROGRESS),
        ),
    )
}
