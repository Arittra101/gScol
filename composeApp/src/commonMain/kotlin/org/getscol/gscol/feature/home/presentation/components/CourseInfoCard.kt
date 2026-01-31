package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import org.getscol.gscol.theme.appColors
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.duration_icon
import scol.composeapp.generated.resources.intake_icon
import scol.composeapp.generated.resources.tution_fee_icon

@Composable
fun CourseInfoCard(
    courseName: String,
    city: String,
    country: String,
    universityName: String,
    universityLogo: String,
    backgroundImage: String,
    intake: String,
    tuitionFees: String,
    duration: String,
    scholarship: String,
    initialDeposit: String,
    ieltsBand: String,
    ieltsSingleBand: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onApplyClick: () -> Unit
) {
    val favoriteIcon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    Surface(
        modifier = Modifier.fillMaxWidth()
            .padding(top = 20.dp),
        color = appColors().customPrimaryContainer,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp)
                        .background(appColors().customBackground)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = universityLogo,
                        contentDescription = "University Logo"
                    )
                }
                Column {
                    Text(
                        text = courseName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = appColors().customPrimaryText,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp, maxFontSize = 15.sp, stepSize = 1.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$city | $universityName | $country",
                        fontSize = 14.sp,
                        color = appColors().customSecondaryText,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp, maxFontSize = 14.sp, stepSize = 1.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp)
            ) {
                // Placeholder for actual image - use AsyncImage or coil in real app
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(appColors().customBackground)
                ) {
                    AsyncImage(
                        model = backgroundImage,
                        contentDescription = "Course Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                }

                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(24.dp)
                        .background(Color.Transparent, CircleShape)
                ) {
                    Icon(
                        favoriteIcon,
                        contentDescription = "Favorite",
                        tint = Color.White,
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 3
            ) {
                InfoChip(
                    iconPath = Res.drawable.intake_icon,
                    title = "Intake",
                    text = intake,
                )
                InfoChip(
                    iconPath = Res.drawable.tution_fee_icon,
                    title = "Tuition fees",
                    text = tuitionFees,
                )
                InfoChip(
                    iconPath = Res.drawable.duration_icon,
                    title = "Duration",
                    text = duration,
                    backgroundColor = appColors().customPrimaryContainer,
                    textColor = appColors().customPrimary,
                    shouldFade = true,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 2
            ) {
                InfoChip(
                    iconPath = Res.drawable.intake_icon,
                    title = "Scholarship",
                    text = scholarship,
                    backgroundColor = appColors().customPrimaryContainer,
                    textColor = appColors().customPrimary,
                    shouldFade = true,
                )
                InfoChip(
                    iconPath = Res.drawable.tution_fee_icon,
                    title = "Initial Deposit",
                    text = initialDeposit,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = appColors().customBackground,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "IELTS Score required: ",
                        fontSize = 12.sp,
                        color = appColors().customPrimary,
                    )
                    Text(
                        text = "$ieltsSingleBand/$ieltsBand",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = appColors().customPrimary,
                    )
                }

                Button(
                    onClick = onApplyClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = appColors().customPrimary.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Apply Now",
                        color = appColors().customPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
