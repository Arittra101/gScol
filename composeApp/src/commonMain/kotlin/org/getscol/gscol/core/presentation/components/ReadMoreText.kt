package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors

@Composable
fun ReadMoreText(
    text: String,
    modifier: Modifier = Modifier,
    maxLinesCollapsed: Int = 4,
    readMoreLabel: String = "Read More",
) {
    val colors = appColors()
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        Text(
            text = text,
            fontSize = 14.sp,
            color = colors.customPrimaryText,
            maxLines = if (expanded) Int.MAX_VALUE else maxLinesCollapsed,
        )
        if (text.length > 120) {
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
            ) {
                Text(
                    text = if (expanded) "Read Less" else readMoreLabel,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.customPrimary,
                )
            }
        }
    }
}
