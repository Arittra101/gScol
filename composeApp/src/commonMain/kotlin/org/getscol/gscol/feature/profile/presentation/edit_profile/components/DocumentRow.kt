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
            .clip(RoundedCornerShape(14.dp))
            .background(colors.customSurface.copy(alpha = 0.55f))
            .padding(horizontal = 12.dp, vertical = 12.dp),
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

            DownloadAction(
                downloadState = downloadState,
                documentStatus = status,
                onDownload = onDownload,
                onCancel = onCancel,
                primaryColor = colors.customPrimary,
            )
        }

        if (downloadState is DownloadState.Downloading) {
            Spacer(modifier = Modifier.height(8.dp))
            val progress = downloadState.progress
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                color = colors.customPrimary,
                trackColor = colors.customPrimary.copy(alpha = 0.15f),
            )
            if (downloadState.totalBytes > 0) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${(progress * 100).toInt()}%  •  ${formatBytes(downloadState.bytesDone)} / ${formatBytes(downloadState.totalBytes)}",
                    fontSize = 11.sp,
                    color = colors.customPrimaryText.copy(alpha = 0.6f),
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
private fun DownloadAction(
    downloadState: DownloadState,
    documentStatus: DocumentStatus,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    primaryColor: Color,
) {
    val (text, action) = when {
        documentStatus == DocumentStatus.Rejected -> "Re-upload" to null
        downloadState is DownloadState.Idle || downloadState is DownloadState.Cancelled -> "Download" to onDownload
        downloadState is DownloadState.RequestingUrl -> "Starting…" to null
        downloadState is DownloadState.Downloading -> "Cancel" to onCancel
        downloadState is DownloadState.Saving -> "Saving…" to null
        downloadState is DownloadState.Completed -> "Open" to onDownload
        downloadState is DownloadState.Failed -> "Retry" to onDownload
        else -> "Download" to onDownload
    }

    Text(
        text = text,
        color = if (action != null) primaryColor else primaryColor.copy(alpha = 0.4f),
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.bodyMedium,
        modifier = if (action != null) Modifier.clickable(onClick = action) else Modifier,
    )
}

private fun formatBytes(bytes: Long): String {
    if (bytes < 1024) return "$bytes B"
    val kb = bytes / 1024.0
    if (kb < 1024) return "%.1f KB".format(kb)
    val mb = kb / 1024.0
    return "%.1f MB".format(mb)
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
