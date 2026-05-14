package org.getscol.gscol.feature.application.presentation.components
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.getscol.gscol.feature.application.presentation.application_details.DocumentTheme
import org.getscol.gscol.feature.application.presentation.application_details.UploadState


private val Brand         = Color(0xFF8B3838)
private val BrandLightest = Color(0xFFFDF7F7)
private val BrandLight    = Color(0xFFF2DCDC)
private val BrandBase     = Color(0xFFDCBFBF)
private val BrandShade    = Color(0xFFB07070)
private val BrandDeepest  = Color(0xFF5C1F1F)

private val CardBackground  = BrandLightest
private val BadgeBackground = BrandLight
private val ProgressTrack   = Color(0xFFEDD5D5)
private val IconTint        = Brand
private val ProgressFill    = Brand
private val TextPrimary     = BrandDeepest
private val TextSecondary   = BrandShade

@Composable
fun FileUploadErrorDialog(
    uploadState: UploadState,
    errorMessage: String = "Something is wrong",
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        FileUploadErrorCard(
            uploadState = uploadState,
            errorMessage = errorMessage,
            onDismiss = onDismiss
        )
    }
}

@Composable
fun FileUploadErrorCard(
    uploadState: UploadState,
    errorMessage: String,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .widthIn(min = 260.dp, max = 340.dp)
            .shadow(
                elevation = 12.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = BrandBase,
                spotColor = BrandShade
            )
            .clip(RoundedCornerShape(20.dp))
            .background(CardBackground)
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {

            // ── File info row ───────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(BadgeBackground)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PictureAsPdf,
                        contentDescription = "PDF file",
                        tint = IconTint,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = uploadState.fileName,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = uploadState.fileSize,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // ── Error message box ───────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFFFCEBEB))
                    .border(1.dp, Color(0xFFF09595), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = null,
                    tint = Color(0xFFA32D2D),
                    modifier = Modifier.size(18.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = "Upload failed",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF501313)
                    )
                    Text(
                        text = errorMessage,
                        fontSize = 12.sp,
                        color = Color(0xFF791F1F),
                        lineHeight = 18.sp
                    )
                }
            }

            // ── Dismiss button ──────────────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFFFFF0F0))
                    .border(1.dp, DocumentTheme.Primary, RoundedCornerShape(9.dp))
                    .clickable(onClick = onDismiss)
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Dismiss",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DocumentTheme.Primary
                )
            }
        }
    }
}