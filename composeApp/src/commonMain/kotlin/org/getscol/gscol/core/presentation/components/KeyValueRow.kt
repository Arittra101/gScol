package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors

@Composable
fun KeyValueRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null,
) {
    val colors = appColors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = colors.customSecondaryText,
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = valueColor ?: colors.customPrimaryText,
        )
    }
}

@Composable
fun KeyValueColumn(
    items: List<Pair<String, String>>,
    modifier: Modifier = Modifier,
    valueColor: ((Int) -> androidx.compose.ui.graphics.Color?)? = null,
) {
    Column(modifier = modifier) {
        items.forEachIndexed { index, (label, value) ->
            KeyValueRow(
                label = label,
                value = value,
                valueColor = valueColor?.invoke(index),
            )
        }
    }
}
