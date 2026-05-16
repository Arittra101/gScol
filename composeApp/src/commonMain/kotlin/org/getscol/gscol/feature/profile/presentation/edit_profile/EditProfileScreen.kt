package org.getscol.gscol.feature.profile.presentation.edit_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.feature.profile.domain.model.AcademicRecordItem
import org.getscol.gscol.feature.profile.domain.model.DocumentStatus
import org.getscol.gscol.feature.profile.presentation.edit_profile.components.DocumentRow
import org.getscol.gscol.feature.profile.presentation.edit_profile.components.ProfileInfoSectionCard
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun EditProfileScreenRoute(
    navigator: Navigator,
) {
    val viewModel: EditProfileViewModel = koinViewModel()
    val uiState by viewModel.state.collectAsState()
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) {
        viewModel.openUrlEvents.collect { url ->
            uriHandler.openUri(url)
        }
    }

    EditProfileScreen(
        uiState = uiState,
        onBack = navigator::navigateBack,
        onView = viewModel::onViewDocumentClick,
    )
}

@Composable
fun EditProfileScreen(
    uiState: EditProfileUiState,
    onBack: () -> Unit,
    onView: (documentId: String) -> Unit,
) {
    val colors = appColors()

    var consultantDialog by remember { mutableStateOf(false) }
    var reuploadDialog by remember { mutableStateOf(false) }

    BaseScreen(
        title = "Edit Information",
        onBackPress = onBack,
        bgColorContent = colors.customSurface,
        showLoader = uiState.isLoading,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 24.dp),
        ) {
            val profile = uiState.profile
            if (profile != null) {
                ProfileHeaderCard(
                    fullName = profile.fullName,
                    subtitle = profile.subtitle,
                    joined = profile.joined,
                    imageUrl = profile.imageUrl,
                )

                Spacer(modifier = Modifier.height(16.dp))

                profile.sections.forEachIndexed { index, section ->
                    ProfileInfoSectionCard(
                        title = section.sectionTitle,
                        isEditable = section.isEditable,
                        rows = section.fields.map { it.label to it.value },
                        onEditClick = if (section.isEditable) ({
                            consultantDialog = true
                        }) else null,
                    )
                    if (index != profile.sections.lastIndex) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                if (profile.academicRecords.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = profile.academicRecordsTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    AcademicRecordsCard(
                        items = profile.academicRecords,
                        loadingDocumentIds = uiState.loadingDocumentIds,
                        documentErrors = uiState.documentErrors,
                        onView = onView,
                        onReUpload = { reuploadDialog = true },
                    )
                }
            } else if (!uiState.errorMessage.isNullOrBlank()) {
                Text(
                    text = uiState.errorMessage,
                    color = colors.customErrorText,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }

    if (consultantDialog) {
        InfoDialog(
            title = "Edit Information",
            message = "To edit your information, please contact with SCOL consultant.",
            onDismiss = { consultantDialog = false },
        )
    }
    if (reuploadDialog) {
        InfoDialog(
            title = "Re-upload Document",
            message = "To re-upload a rejected document, please contact with SCOL support.",
            onDismiss = { reuploadDialog = false },
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    fullName: String,
    subtitle: String?,
    joined: String?,
    imageUrl: String?,
) {
    val colors = appColors()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(vertical = 16.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(86.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(colors.customSurface),
            contentAlignment = Alignment.Center,
        ) {
            if (!imageUrl.isNullOrBlank()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Profile photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(66.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFD7B9A0)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = fullName.trim().take(1).ifBlank { "S" },
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = fullName,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = colors.customPrimaryText,
        )

        if (!subtitle.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = colors.customPrimaryText.copy(alpha = 0.6f),
            )
        }

        if (!joined.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Joined $joined",
                fontSize = 12.sp,
                color = colors.customPrimaryText.copy(alpha = 0.6f),
            )
        }
    }
}

@Composable
private fun AcademicRecordsCard(
    items: List<AcademicRecordItem>,
    loadingDocumentIds: Set<String>,
    documentErrors: Map<String, String>,
    onView: (documentId: String) -> Unit,
    onReUpload: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp, color = Color(0xFFE5E7EB),
                shape = RoundedCornerShape(16.dp)
            )
            .background(Color.White)
    ) {

        items.forEachIndexed { index, item ->
            DocumentRow(
                label = item.label,
                status = item.status,
                isLoading = item.id in loadingDocumentIds,
                errorText = documentErrors[item.id],
                onView = {
                    when (item.status) {
                        DocumentStatus.InProgress,
                        DocumentStatus.Verified -> onView(item.id)

                        DocumentStatus.Rejected -> onReUpload()
                    }
                },
            )
            if (index < items.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    thickness = 1.dp,
                    color = Color(0xFFE5E7EB),
                )
            }
        }
    }
}

@Composable
private fun InfoDialog(
    title: String,
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("OK") }
        },
        title = {
            Text(text = title, fontWeight = FontWeight.SemiBold)
        },
        text = {
            Text(text = message, textAlign = TextAlign.Start)
        },
    )
}
