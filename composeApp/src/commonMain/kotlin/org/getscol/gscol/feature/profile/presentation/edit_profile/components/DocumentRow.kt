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
import androidx.compose.material3.LinearProgressIndicator
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
import org.getscol.gscol.feature.profile.domain.download.DownloadState
import org.getscol.gscol.feature.profile.domain.model.DocumentStatus
import org.getscol.gscol.theme.appColors

@Composable
fun DocumentRow(
    label: String,
    status: DocumentStatus,
    downloadState: DownloadState,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
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
                // Icon circle — soft rose/pink tint matching screenshot
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
                downloadState = downloadState,
                documentStatus = status,
                onDownload = onDownload,
                onCancel = onCancel,
            )
        }

        // Progress bar while downloading
        if (downloadState is DownloadState.Downloading) {
            Spacer(modifier = Modifier.height(10.dp))
            val progress = downloadState.progress
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = Color(0xFFB91C1C),
                trackColor = Color(0xFFFCE8E8),
            )
            if (downloadState.totalBytes > 0) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${(progress * 100).toInt()}%  •  ${formatBytes(downloadState.bytesDone)} / ${
                        formatBytes(
                            downloadState.totalBytes
                        )
                    }",
                    fontSize = 11.sp,
                    color = colors.customPrimaryText.copy(alpha = 0.55f),
                )
            }
        }

        if (downloadState is DownloadState.Failed) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Download failed. Tap Retry.",
                fontSize = 11.sp,
                color = Color(0xFFDC2626),
            )
        }

        if (downloadState is DownloadState.Completed) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Saved to Downloads",
                fontSize = 11.sp,
                color = Color(0xFF059669),
            )
        }
    }
}

@Composable
private fun ActionButton(
    downloadState: DownloadState,
    documentStatus: DocumentStatus,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
) {
    val isRejected = documentStatus == DocumentStatus.Rejected

    val (text, action) = when {
        isRejected -> "Re-upload" to onDownload
        downloadState is DownloadState.Idle
                || downloadState is DownloadState.Cancelled -> "View" to onDownload

        downloadState is DownloadState.RequestingUrl -> "Starting…" to null
        downloadState is DownloadState.Downloading -> "Cancel" to onCancel
        downloadState is DownloadState.Saving -> "Saving…" to null
        downloadState is DownloadState.Completed -> "Open" to onDownload
        downloadState is DownloadState.Failed -> "Retry" to onDownload
        else -> "View" to onDownload
    }

    val isEnabled = action != null

    if (isRejected) {
        // Filled dark-red pill (matches the "Re-upload" button in the screenshot)
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
        DocumentStatus.InProgress -> Color(0xFF3B82F6) to "Uploaded"      // blue dot
        DocumentStatus.Verified -> Color(0xFF22C55E) to "Verified"      // green dot
        DocumentStatus.Rejected -> Color(0xFFEF4444) to "Rejected"      // red dot
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
            color = Color(0xFF6B7280), // muted grey, matching screenshot
            fontWeight = FontWeight.Normal,
        )
    }
}

private fun formatBytes(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024) return "${(kb * 10).toLong() / 10.0} KB"
    val mb = kb / 1024.0
    return "${(mb * 10).toLong() / 10.0} MB"
}