package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.theme.appColors

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    showInfoIcon: Boolean = false,
    onInfoClick: (() -> Unit)? = null,
) {
    val colors = appColors()
    Row(
        modifier = modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = colors.customPrimaryText,
        )
        if (showInfoIcon) {
            IconButton(
                onClick = { onInfoClick?.invoke() },
                modifier = Modifier.padding(start = 4.dp),
            ) {
                Icon(
                    modifier= Modifier.width(20.dp),
                    imageVector = Icons.Default.Info,
                    contentDescription = "Info",
                    tint = colors.customPrimary,
                )
            }
        }
    }
}
