package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors

@Composable
fun ScrollableTabs(
    tabs: List<String>,
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    /**
     * When non-zero, the scroll strip is measured [2 × outdent] wider than the parent slot and
     * shifted so it visually spans the full screen width (breaks out of parent horizontal padding).
     */
    horizontalOutdent: Dp = 0.dp,
    /** Extra scrollable space before the first tab (use with [horizontalOutdent] to align with padded content). */
    scrollLeadingPadding: Dp = 0.dp,
    /** Extra scrollable space after the last tab. */
    scrollTrailingPadding: Dp = 0.dp,
) {
    val colors = appColors()
    val scrollState = rememberScrollState()
    HorizontalPaddingBleed(outdent = horizontalOutdent, modifier = modifier) {
        Row(
            modifier = Modifier
                .horizontalScroll(scrollState)
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            if (scrollLeadingPadding > 0.dp) {
                Spacer(modifier = Modifier.width(scrollLeadingPadding))
            }
            tabs.forEachIndexed { index, title ->
                val selected = index == selectedIndex
                Surface(
                    modifier = Modifier.clickable { onTabSelected(index) },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(
                        1.dp,
                        if (selected) colors.customPrimary else colors.customSecondary
                    ),
                    color = if (selected) colors.customPrimary else colors.customSurface,
                ) {
                    Text(
                        text = title,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (selected) Color.White else colors.customPrimaryText,
                    )
                }
            }
            if (scrollTrailingPadding > 0.dp) {
                Spacer(modifier = Modifier.width(scrollTrailingPadding))
            }
        }
    }
}
