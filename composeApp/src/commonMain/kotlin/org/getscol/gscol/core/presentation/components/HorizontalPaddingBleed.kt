package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Lets horizontal scroll content (e.g. [LazyRow], [horizontalScroll])
 * use the full screen width while this node still occupies only the parent’s padded width.
 *
 * @param outdent Should match the parent’s horizontal padding so edges align with the screen.
 */
@Composable
fun HorizontalPaddingBleed(
    outdent: Dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier.fillMaxWidth(),
        content = content,
    ) { measurables, constraints ->
        val bleedPx = if (outdent > 0.dp && constraints.hasBoundedWidth) outdent.roundToPx() else 0
        val measurable = measurables.single()
        val childConstraints = if (bleedPx > 0) {
            constraints.copy(maxWidth = constraints.maxWidth + 2 * bleedPx)
        } else {
            constraints
        }
        val placeable = measurable.measure(childConstraints)
        val offsetX = if (bleedPx > 0) -bleedPx else 0
        layout(constraints.maxWidth.coerceAtLeast(0), placeable.height) {
            placeable.placeRelative(offsetX, 0)
        }
    }
}
