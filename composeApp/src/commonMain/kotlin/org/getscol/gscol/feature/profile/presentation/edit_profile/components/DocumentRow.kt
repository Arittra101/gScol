package org.getscol.gscol.feature.profile.presentation.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.profile.domain.model.DocumentStatus
import org.getscol.gscol.theme.appColors

@Composable
fun DocumentRow(
    label: String,
    status: DocumentStatus,
    isLoading: Boolean,
    errorText: String?,
    onView: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = appColors()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFCE8E8)),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = Color(0xFFB91C1C),
                        modifier = Modifier.size(22.dp),
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = colors.customPrimaryText,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    StatusDotLabel(status = status)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            ActionButton(
                isLoading = isLoading,
                errorText = errorText,
                documentStatus = status,
                onView = onView,
            )
        }

        if (!errorText.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorText,
                fontSize = 11.sp,
                color = Color(0xFFDC2626),
            )
        }
    }
}

@Composable
private fun ActionButton(
    isLoading: Boolean,
    errorText: String?,
    documentStatus: DocumentStatus,
    onView: () -> Unit,
) {
    val isRejected = documentStatus == DocumentStatus.Rejected

    val (text, action) = when {
        isRejected -> "Re-upload" to onView
        isLoading -> "Opening..." to null
        !errorText.isNullOrBlank() -> "Retry" to onView
        else -> "View" to onView
    }

    val isEnabled = action != null

    if (isRejected) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(if (isEnabled) Color(0xFF991B1B) else Color(0xFF991B1B).copy(alpha = 0.4f))
                .then(if (isEnabled) Modifier.clickable(onClick = action) else Modifier)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    } else {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(999.dp))
                .background(Color(0xFFFCE8E8))
                .then(if (isEnabled) Modifier.clickable(onClick = action) else Modifier)
                .padding(horizontal = 18.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = text,
                color = if (isEnabled) Color(0xFFB91C1C) else Color(0xFFB91C1C).copy(alpha = 0.4f),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun StatusDotLabel(
    status: DocumentStatus,
    modifier: Modifier = Modifier,
) {
    val (dotColor, text) = when (status) {
        DocumentStatus.InProgress -> Color(0xFF3B82F6) to "Uploaded"
        DocumentStatus.Verified -> Color(0xFF22C55E) to "Verified"
        DocumentStatus.Rejected -> Color(0xFFEF4444) to "Rejected"
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(dotColor),
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 13.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.Normal,
        )
    }
}
