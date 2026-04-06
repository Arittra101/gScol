package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors


@Composable
fun HomeSearchBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val colors = appColors()
    val rowModifier = modifier
        .height(36.dp)
        .padding(start = 12.dp)
        .background(
            color = colors.customPrimaryContainer,
            shape = RoundedCornerShape(50.dp)
        )
        .border(
            width = 1.dp,
            color = colors.customInfo,
            shape = RoundedCornerShape(50.dp)
        )
        .padding(horizontal = 12.dp)
        .then(
            Modifier.clickable(onClick = onClick)
        )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.customInfo,
            modifier = Modifier.size(18.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))


        Text(
            "Search country, course, intake",
            fontSize = 11.sp,
            color = colors.customInfo,
            modifier = Modifier.weight(1f),
            autoSize = TextAutoSize.StepBased(
                minFontSize = 8.sp,
                maxFontSize = 14.sp,
                stepSize = 1.sp
            )
        )
    }
}
