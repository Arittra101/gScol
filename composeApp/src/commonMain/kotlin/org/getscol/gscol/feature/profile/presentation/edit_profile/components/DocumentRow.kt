package org.getscol.gscol.feature.profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import org.getscol.gscol.feature.profile.domain.model.DocumentStatus
import org.getscol.gscol.theme.appColors

@Composable
fun DocumentRow(
    label: String,
    status: DocumentStatus,
    actionText: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = appColors()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(colors.customSurface.copy(alpha = 0.55f))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(colors.customPrimary.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = colors.customPrimary,
                    modifier = Modifier.size(18.dp),
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.customPrimaryText,
                )
                StatusPill(status = status)
            }
        }

        Text(
            text = actionText,
            color = colors.customPrimary,
            fontWeight = FontWeight.SemiBold,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable(onClick = onActionClick),
        )
    }
}

@Composable
private fun StatusPill(
    status: DocumentStatus,
    modifier: Modifier = Modifier,
) {
    val (bg, fg, text) = when (status) {
        DocumentStatus.InProgress -> Triple(Color(0xFFEFF6FF), Color(0xFF2563EB), "In progress")
        DocumentStatus.Verified -> Triple(Color(0xFFECFDF5), Color(0xFF059669), "Verified")
        DocumentStatus.Rejected -> Triple(Color(0xFFFEF2F2), Color(0xFFDC2626), "Rejected")
    }
    Box(
        modifier = modifier
            .padding(top = 6.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(bg)
            .padding(horizontal = 10.dp, vertical = 4.dp),
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = fg,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

