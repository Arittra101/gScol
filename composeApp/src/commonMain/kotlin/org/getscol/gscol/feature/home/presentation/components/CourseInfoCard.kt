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
import org.getscol.gscol.feature.home.presentation.HomeAction
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.duration_icon
import scol.composeapp.generated.resources.intake_icon
import scol.composeapp.generated.resources.tution_fee_icon

@Composable
fun CourseInfoCard(
    courseId : String,
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
) {
    val favoriteIcon = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE6E6FA)),
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
                        color = Color(0xFF0D171B),
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
                        color = Color(0xFF4C809A),
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
                        .background(Color(0xFFCCCCCC))
                ) {
                    AsyncImage(
                        model = backgroundImage,
                        contentDescription = "Course Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.FillBounds,
                    )
                }

                IconButton(
                    onClick = { action(HomeAction.AddToWishlist(courseId = courseId, isWishListed = isFavorite)) },
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(16.dp)
                        .size(24.dp)
                        .background(if(!isFavorite)Color.Transparent else Color.Red, CircleShape)
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
                    backgroundColor = Color.White,
                    textColor = Color(0xFF8B0000),
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
                    backgroundColor = Color.White,
                    textColor = Color(0xFF8B0000),
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
                    .padding(horizontal = 16.dp)
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
                        color = Color(0xFF8B0000)
                    )
                    Text(
                        text = "$ieltsSingleBand/$ieltsBand",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B0000)
                    )
                }

                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF8B0000).copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(4.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Apply Now",
                        color = Color(0xFF8B0000),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
