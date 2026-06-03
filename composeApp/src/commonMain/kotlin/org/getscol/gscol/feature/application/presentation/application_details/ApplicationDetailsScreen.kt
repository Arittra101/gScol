package org.getscol.gscol.feature.application.presentation.application_details

import FileUploadDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.ApiResponseBottomSheet
import org.getscol.gscol.feature.application.presentation.application_details.components.ApplicationActionButtons
import org.getscol.gscol.feature.application.presentation.application_details.components.ApplicationInfoCard
import org.getscol.gscol.feature.application.presentation.application_details.components.DocumentProgressStepper
import org.getscol.gscol.feature.application.presentation.application_details.components.DocumentsSection
import org.getscol.gscol.feature.application.presentation.application_details.components.UniversityHeroHeader
import org.getscol.gscol.feature.application.presentation.application_details.components.documentProgressBottomInset
import org.getscol.gscol.feature.application.presentation.components.WithdrawApplicationBottomSheet
import org.getscol.gscol.feature.application.presentation.toDocumentProgressSteps
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


object DocumentTheme {
    val Primary = Color(0xFF8B3838)
    val Background = Color(0xFFF2F2F7)
    val CardBackground = Color(0xFFFFFFFF)
    val CardBorder = Color(0xFFE5E5EA)
    val ExpandedSurface = Color(0xFFFAFAFA)
    val DividerColor = Color(0xFFF0F0F0)
    val TextPrimary = Color(0xFF1C1C1E)
    val TextSecondary = Color(0xFF8E8E93)
    val Verified = Color(0xFF34C759)
    val InProgress = Color(0xFFFF9500)
    val Pending = Color(0xFF8E8E93)
    val IconBgVerified = Color(0xFFF0F9F0)
    val IconBgInProgress = Color(0xFFFFF8EC)
    val IconBgPending = Color(0xFFF5F5F5)
    val FileTileBorder = Color(0xFFF0F0F0)
}

@Composable
fun ApplicationDetailsRoute(
    navigator: Navigator,
    applicationId: String,
    viewmodel: ApplicationDetailsViewmodel = koinViewModel(
        key = "ApplicationDetails-$applicationId",
        parameters = { parametersOf(applicationId) })
) {

    val state by viewmodel.applicationState.collectAsState()
    val action = viewmodel::onAction

    ObserveEffect(viewmodel.applicationDetailsUiEffect) { effect ->
        when (effect) {
            is ApplicationDetailsUiEffect.NavigateToApplicationTracker -> {
                navigator.navigateTo(Route.ApplicationStatusTrackerRoute(effect.applicationId))
            }
        }
    }

    if (state.showDocumentUploadLoader) {
        state.uploadState?.let {
            FileUploadDialog(uploadState = it, onCancel = {
                action(ApplicationDetailAction.OnCancelUpload)
            })
        }
    }

    WithdrawApplicationBottomSheet(showBottomSheet = state.showConsultantBottomSheet){
        action(ApplicationDetailAction.OnHideWithdrawBottomSheet)
    }

    ApiResponseBottomSheet(
        showBottomSheet = state.showFileUpDownloadBottomSheet,
        isSuccess = state.isFileSuccessResponse,
        message = state.fileBottomSheetMsg,
        onDismiss = { action(ApplicationDetailAction.OnHideDocumentResponseBottomSheet) },
    )

    BaseScreen(
        title = "Application Tracker",
        showLoader = state.isLoading,
        onBackPress = { navigator.navigateBack() }) { paddingValues ->
        ApplicationDetailsScreen(state,action)
    }

}

private const val DocumentProgressScrollThreshold = 8
private const val DocumentProgressAnimationDurationMillis = 280

@Composable
private fun rememberDocumentProgressVisibleOnScroll(scrollState: ScrollState): Boolean {
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(scrollState) {
        var previousScroll = scrollState.value
        snapshotFlow { scrollState.value }.collect { current ->
            val delta = current - previousScroll
            isVisible = when {
                current <= 0 -> true
                delta > DocumentProgressScrollThreshold -> true
                delta < -DocumentProgressScrollThreshold -> false
                else -> isVisible
            }
            previousScroll = current
        }
    }

    return isVisible
}

@Preview
@Composable
fun ApplicationDetailsScreen(
    state: ApplicationDetailsUiState,
    action: (ApplicationDetailAction) -> Unit
) {
    val showDocumentProgress = !state.isLoading && state.documentCheckLists.isNotEmpty()
    val scrollState = rememberScrollState()
    val isStepperVisibleOnScroll = rememberDocumentProgressVisibleOnScroll(scrollState)
    val isStepperVisible = showDocumentProgress && isStepperVisibleOnScroll

    val bottomInset by animateDpAsState(
        targetValue = documentProgressBottomInset(isStepperVisible),
        animationSpec = tween(durationMillis = DocumentProgressAnimationDurationMillis),
        label = "document_progress_bottom_inset",
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DocumentTheme.Background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = bottomInset),
        ) {
            UniversityHeroHeader(
                imageUrl = state.universityCoverImageUrl.orEmpty(),
                universityName = state.universityName.orEmpty(),
            )

            ApplicationInfoCard(
                intake = "${state.intakeMonth ?: ""} ${state.intakeYear}",
                program = state.courseName.orEmpty(),
                applicationId = state.applicationSerialNumber.orEmpty(),
            )

            DocumentsSection(documentCheckList = state.documentCheckLists, action)

            ApplicationActionButtons(
                onWithdrawClick = { action(ApplicationDetailAction.OnWithdrawApplication) },
                onTrackApplicationClick = { action(ApplicationDetailAction.OnTrackApplication) },
            )
        }

        AnimatedVisibility(
            visible = isStepperVisible,
            modifier = Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = tween(durationMillis = DocumentProgressAnimationDurationMillis),
            ) + fadeIn(
                animationSpec = tween(durationMillis = DocumentProgressAnimationDurationMillis),
            ),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(durationMillis = DocumentProgressAnimationDurationMillis),
            ) + fadeOut(
                animationSpec = tween(durationMillis = DocumentProgressAnimationDurationMillis),
            ),
        ) {
            DocumentProgressStepper(
                steps = state.documentCheckLists.toDocumentProgressSteps(),
            )
        }
    }
}
