package org.getscol.gscol.feature.consultant.presentation.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.theme.appColors
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import scol.composeapp.generated.resources.Res
import scol.composeapp.generated.resources.fardeen_ishaque
import scol.composeapp.generated.resources.raihan_ul_islam
import scol.composeapp.generated.resources.shafayat_jamil

@Composable
fun ConsultantDetailsScreenRoot(
    consultantId: String,
    navigator: Navigator,
    viewModel: ConsultantDetailsViewModel = koinViewModel(
        key = "ConsultantDetails-$consultantId",
        parameters = { parametersOf(consultantId) },
    ),
) {
    val state by viewModel.state.collectAsState()
    ConsultantDetailsScreen(
        state = state,
        onBack = navigator::navigateBack,
    )
}

@Composable
fun ConsultantDetailsScreen(
    state: ConsultantDetailsState,
    onBack: () -> Unit,
) {
    val colors = appColors()
    BaseScreen(
        title = "Consultant Profile",
        bgColorContent = colors.customSurface,
        onBackPress = onBack,
        showLoader = state.isLoading,
    ) {
        if (state.consultant == null) return@BaseScreen

        val consultant = state.consultant
        /// get image resource
        val imageResource = when (consultant.image) {
            "raihan_ul_islam" -> painterResource(Res.drawable.raihan_ul_islam)
            "fardeen_ishaque" -> painterResource(Res.drawable.fardeen_ishaque)
            "shafayat_jamil" -> painterResource(Res.drawable.shafayat_jamil)
            else -> painterResource(Res.drawable.raihan_ul_islam)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = imageResource,
                contentDescription = "Consultant Image",
                modifier = Modifier.size(128.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = consultant.name,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colors.customPrimaryText
            )
            Text(
                text = consultant.title.uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = colors.customPrimary,
                letterSpacing = 0.8.sp
            )
            Text(
                text = consultant.organization,
                fontSize = 13.sp,
                color = colors.customPrimaryText,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = consultant.bio,
                fontSize = 15.sp,
                color = Color(0xFF44403C),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
            )

            Spacer(modifier = Modifier.height(20.dp))

            // ── Certification ─────────────────────────────────────────────────
            ProfileSectionHeader("CERTIFICATION")
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = colors.customBackground),
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(colors.customPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🛡", fontSize = 18.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = consultant.certification.issuedBy.uppercase(),
                            fontSize = 10.sp,
                            color = colors.customInfo,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = consultant.certification.title,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colors.customPrimaryText
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(32.dp)) {
                            Column {
                                Text(
                                    text = "ISSUED",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.customInfo,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = consultant.certification.issuedDate,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.customPrimaryText
                                )
                            }
                            Column {
                                Text(
                                    text = "ROLE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.customInfo,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "Certified ${consultant.certification.role}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = colors.customPrimaryText
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "CERTIFICATE ID",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = colors.customInfo,
                            letterSpacing = 0.8.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = colors.customPrimary.copy(alpha = 0.1f),
                            modifier = Modifier.padding(top = 2.dp)
                        ) {
                            Text(
                                text = consultant.certification.certificateCode,
                                fontSize = 12.sp,
                                color = colors.customPrimary,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ── Contact ───────────────────────────────────────────────────────
            ProfileSectionHeader("CONTACT")
            Spacer(modifier = Modifier.height(8.dp))

            ContactRow(icon = Icons.Default.Phone, label = "PHONE", value = consultant.phone)
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(icon = Icons.Default.Email, label = "EMAIL", value = consultant.email)
            Spacer(modifier = Modifier.height(8.dp))
            ContactRow(
                icon = Icons.Default.Schedule,
                label = "OFFICE HOURS",
                value = consultant.officeHours
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun ProfileSectionHeader(label: String) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = appColors().customPrimary,
            letterSpacing = 0.8.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(modifier = Modifier.weight(1f), color = appColors().customPrimary)
    }
}

@Composable
private fun ContactRow(icon: ImageVector, label: String, value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = appColors().customBackground),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape)
                    .background(appColors().customPrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = appColors().customPrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = appColors().customInfo,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = value,
                    fontSize = 13.sp,
                    color = appColors().customPrimaryText,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
