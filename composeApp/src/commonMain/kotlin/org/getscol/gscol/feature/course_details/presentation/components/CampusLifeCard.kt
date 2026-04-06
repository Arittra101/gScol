package org.getscol.gscol.feature.course_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.feature.course_details.domain.model.CampusLifeItem
import org.getscol.gscol.theme.appColors

@Composable
fun CampusLifeCard(
    item: CampusLifeItem,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    val colors = appColors()
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (onClick != null) Modifier.clickable(onClick = onClick)
                else Modifier
            ),
        shape = RoundedCornerShape(16.dp),
    ) {
        val thumbnail = item.thumbnailUrl
            ?: item.videoUrl?.let { youtubeThumbnailFromUrl(it) }

        val metaLine = if (item.duration != null || item.count != null) {
            buildString {
                item.duration?.let { append("$it ") }
                item.count?.let { append(it) }
            }.trim()
        } else {
            null
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .background(colors.customSecondary),
        ) {
            if (thumbnail != null) {
                AsyncImage(
                    model = thumbnail,
                    contentDescription = item.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .height(88.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.72f),
                            ),
                        ),
                    ),
            )
            if (item.isVideo) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    modifier = Modifier.align(Alignment.Center),
                    tint = Color.White,
                )
            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp),
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    maxLines = 2,
                )
                if (metaLine != null) {
                    Text(
                        text = metaLine,
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.85f),
                    )
                }
            }
        }
    }
}

private fun youtubeThumbnailFromUrl(url: String): String? {
    // Supports typical YouTube URL forms: https://www.youtube.com/watch?v=ID, https://youtu.be/ID, /embed/ID
    val id = extractYoutubeId(url) ?: return null
    return "https://img.youtube.com/vi/$id/mqdefault.jpg"
}

private fun extractYoutubeId(url: String): String? {
    // Very small, pragmatic extractor – enough for backend-provided clean URLs
    val watchPrefix = "v="
    val shortPrefix = "youtu.be/"
    val embedPrefix = "/embed/"

    return when {
        url.contains(shortPrefix) -> {
            url.substringAfter(shortPrefix).substringBeforeAny('&', '?', '/')
        }

        url.contains(embedPrefix) -> {
            url.substringAfter(embedPrefix).substringBeforeAny('&', '?', '/')
        }

        url.contains(watchPrefix) -> {
            url.substringAfter(watchPrefix).substringBeforeAny('&', '#')
        }

        else -> null
    }
}

private fun String.substringBeforeAny(vararg delimiters: Char): String {
    var end = length
    for (d in delimiters) {
        val idx = indexOf(d)
        if (idx in 0 until end) end = idx
    }
    return substring(0, end)
}
