package org.getscol.gscol.feature.course_details.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.core.presentation.components.PillTag
import org.getscol.gscol.theme.appColors

@Composable
fun CourseOverviewCard(
    courseName: String,
    ranking: String?,
    universityName: String,
    universityLogoUrl: String,
    establishedYear: String?,
    institutionType: String,
    location: String,
    modifier: Modifier = Modifier,
    onRankingInfoClick: (() -> Unit)? = null,
) {
    val colors = appColors()
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = colors.customPrimaryContainer,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
        ) {
            // Row: [courseName + divider] <spaceBetween> [university logo]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    Text(
                        text = courseName,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.customPrimaryText,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 12.sp,
                            maxFontSize = 22.sp,
                            stepSize = 1.sp,
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        color = colors.customSecondary,
                    )
                }
                Spacer(modifier = Modifier.size(12.dp))
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colors.customSecondary),
                ) {
                    AsyncImage(
                        model = universityLogoUrl,
                        contentDescription = "University logo",
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            // Row: ranking, info icon, dot, university name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                ranking?.let { rank ->
                    Text(
                        text = rank,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.customPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 10.sp,
                            maxFontSize = 20.sp,
                            stepSize = 1.sp,
                        ),
                    )
                    IconButton(
                        onClick = { onRankingInfoClick?.invoke() },
                        modifier = Modifier.size(20.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Ranking info",
                            modifier = Modifier.size(16.dp),
                            tint = colors.customPrimary,
                        )
                    }
                    Text(
                        text = "•",
                        fontSize = 12.sp,
                        color = colors.customSecondaryText,
                        modifier = Modifier.padding(horizontal = 2.dp),
                    )
                }
                Text(
                    text = universityName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colors.customPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                    autoSize = TextAutoSize.StepBased(
                        minFontSize = 10.sp,
                        maxFontSize = 16.sp,
                        stepSize = 1.sp,
                    ),
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                establishedYear?.let { year ->
                    PillTag(
                        text = year,
                        backgroundColor = colors.customPrimary.copy(alpha = 0.2f),
                        textColor = colors.customPrimary,
                    )
                }
                PillTag(text = institutionType)
                PillTag(
                    text = location,
                    leadingIcon = Icons.Default.LocationOn,
                )
            }
        }
    }
}
