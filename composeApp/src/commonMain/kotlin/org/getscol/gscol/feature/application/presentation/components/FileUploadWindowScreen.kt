import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.getscol.gscol.feature.application.presentation.application_details.DocumentTheme
import org.getscol.gscol.feature.application.presentation.application_details.UploadState
import org.jetbrains.compose.ui.tooling.preview.Preview


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

@Preview
@Composable
fun FileUploadDialog(
    uploadState: UploadState,
    onCancel: () -> Unit,
    onDismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        FileUploadCard(
            uploadState = uploadState,
            onCancel = onCancel
        )
    }
}

@Preview
@Composable
fun FileUploadCard(
    uploadState: UploadState,
    modifier: Modifier = Modifier,
    onCancel: (() -> Unit) = {}
) {
    val animatedProgress by animateFloatAsState(
        targetValue = uploadState.progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 600),
        label = "upload_progress"
    )

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

                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = uploadState.fileName,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                    Text(
                        text = uploadState.fileSize,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }

            // ── Status + progress ───────────────────────────────────────────
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Uploading to Start",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${(animatedProgress * 100).toInt()}%",
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(50)),
                    color = ProgressFill,
                    trackColor = ProgressTrack
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(9.dp))
                    .background(Color(0xFFFFF0F0))
                    .border(1.dp, DocumentTheme.Primary, RoundedCornerShape(9.dp))
                    .clickable(onClick = onCancel)
                    .padding(vertical = 13.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cancel Upload",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DocumentTheme.Primary
                )
            }

        }
    }
}