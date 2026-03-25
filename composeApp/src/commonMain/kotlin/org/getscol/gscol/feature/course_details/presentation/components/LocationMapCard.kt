package org.getscol.gscol.feature.course_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.getscol.gscol.theme.appColors

@Composable
fun LocationMapCard(
    modifier: Modifier = Modifier,
    mapImageUrl: String? = null,
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
        shape = RoundedCornerShape(12.dp),
        color = colors.customSecondary.copy(alpha = 0.5f),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(colors.customSecondary.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center,
        ) {
            if (!mapImageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = mapImageUrl,
                    contentDescription = "Map preview",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
            if (mapImageUrl.isNullOrBlank()) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    modifier = Modifier.size(48.dp),
                    tint = colors.customPrimary,
                )
            }
        }
    }
}
