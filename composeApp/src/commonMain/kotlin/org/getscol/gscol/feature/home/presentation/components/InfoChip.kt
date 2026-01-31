package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun InfoChip(
    iconPath: DrawableResource,
    title: String,
    text: String,
    backgroundColor: Color? = null,
    textColor: Color? = null,
    borderColor: Color? = null,
    shouldFade: Boolean = false,
) {
    val colors = appColors()
    val effectiveBackgroundColor = backgroundColor ?: colors.customPrimary
    val effectiveTextColor = textColor ?: colors.customPrimaryContainer
    val effectiveBorderColor = borderColor ?: colors.customPrimary

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = effectiveBackgroundColor,
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val brush =
                    if (shouldFade) {
                        Brush.horizontalGradient(
                            colors = listOf(
                                effectiveBorderColor,
                                effectiveBorderColor,
                                effectiveBorderColor.copy(alpha = 0f)
                            )
                        )
                    } else {
                        Brush.linearGradient(listOf(effectiveBorderColor, effectiveBorderColor))
                    }

                drawRoundRect(
                    brush = brush,
                    size = size,
                    cornerRadius = CornerRadius(20.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }

    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconPath),
                contentDescription = null,
                tint = effectiveTextColor,
                modifier = Modifier
                    .padding(end = 2.dp)
                    .size(14.dp)
            )
            Text(
                text = "$title: ",
                fontSize = 10.sp,
                color = effectiveTextColor,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = text,
                fontSize = 11.sp,
                color = effectiveTextColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}