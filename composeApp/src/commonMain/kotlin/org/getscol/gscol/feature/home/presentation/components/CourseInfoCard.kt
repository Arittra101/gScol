package org.getscol.gscol.feature.home.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.remember
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
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.theme.appColors
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.duration_icon
import scol.composeapp.generated.resources.intake_icon
import scol.composeapp.generated.resources.tution_fee_icon

// Memoize constant colors and styles to avoid recreation
private val DIVIDER_COLOR_CARD = Color(0xFFF0F0F0)
private val BUTTON_CONTENT_PADDING = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
private val BUTTON_SHAPE = RoundedCornerShape(4.dp)
private val ICON_BUTTON_SIZE = 24.dp
private val FAVORITE_TINT_FAVORITED = Color.Red
private val FAVORITE_TINT_UNFAVORITED = Color.White

@Composable
fun CourseInfoCard(
    courseId: String,
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
    action: (HomeAction) -> Unit,
    onCourseClick: () -> Unit,
) {
    val colors = appColors()

    // Only memoize expensive callback - simple conditionals don't need remember
    val onWishlistClick = remember(courseId, isFavorite, action) {
        {
            action(
                HomeAction.AddToWishlist(
                    courseId = courseId,
                    isWishListed = isFavorite
                )
            )
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // Header Row with Logo and Course Info
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp)
                        .background(colors.customBackground)
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
                        color = colors.customPrimaryText,
                        maxLines = 1,
                        lineHeight = 24.sp,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp, maxFontSize = 15.sp, stepSize = 1.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                    Text(
                        text = "$city | $universityName | $country",
                        fontSize = 14.sp,
                        color = colors.customSecondaryText,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        autoSize = TextAutoSize.StepBased(
                            minFontSize = 8.sp, maxFontSize = 14.sp, stepSize = 1.sp
                        ),
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            // Background Image with Favorite Button
            Box(
                modifier = Modifier.fillMaxWidth().height(200.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize()
                        .background(colors.customBackground)
                ) {
                    AsyncImage(
                        model = backgroundImage,
                        contentDescription = "Course Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                }

                IconButton(
                    onClick = onWishlistClick,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(ICON_BUTTON_SIZE)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) FAVORITE_TINT_FAVORITED else FAVORITE_TINT_UNFAVORITED
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // First Info Chips Row
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoChip(
                    modifier = Modifier.weight(1f),
                    iconPath = Res.drawable.intake_icon,
                    title = "Intake",
                    text = intake,
                )
                InfoChip(
                    modifier = Modifier.weight(1f),
                    iconPath = Res.drawable.tution_fee_icon,
                    title = "Tuition",
                    text = tuitionFees,
                )
                InfoChip(
                    modifier = Modifier.weight(1f),
                    iconPath = Res.drawable.duration_icon,
                    title = "Duration",
                    text = duration,
                    backgroundColor = colors.customPrimaryContainer,
                    textColor = colors.customPrimary,
                    shouldFade = true,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Second Info Chips Row
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                InfoChip(
                    modifier = Modifier.weight(1f),
                    iconPath = Res.drawable.intake_icon,
                    title = "Scholarship",
                    text = scholarship,
                    backgroundColor = colors.customPrimaryContainer,
                    textColor = colors.customPrimary,
                    shouldFade = true,
                )
                InfoChip(
                    modifier = Modifier.weight(1f),
                    iconPath = Res.drawable.tution_fee_icon,
                    title = "Initial Deposit",
                    text = initialDeposit,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = DIVIDER_COLOR_CARD,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Row with IELTS Score and Apply Button
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
                        color = colors.customPrimary,
                    )
                    Text(
                        text = "$ieltsSingleBand/$ieltsBand",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = colors.customPrimary,
                    )
                }

                Button(
                    onClick = onCourseClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.customPrimary.copy(alpha = 0.15f)
                    ),
                    shape = BUTTON_SHAPE,
                    contentPadding = BUTTON_CONTENT_PADDING
                ) {
                    Text(
                        text = "Apply Now",
                        color = colors.customPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
