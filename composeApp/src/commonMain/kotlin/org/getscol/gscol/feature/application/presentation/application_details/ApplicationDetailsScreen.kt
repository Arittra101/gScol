package org.getscol.gscol.feature.application.presentation.application_details

import FileUploadDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.feature.application.presentation.application_details.components.ApplicationActionButtons
import org.getscol.gscol.feature.application.presentation.application_details.components.ApplicationInfoCard
import org.getscol.gscol.feature.application.presentation.application_details.components.DocumentsSection
import org.getscol.gscol.feature.application.presentation.application_details.components.UniversityHeroHeader
import org.getscol.gscol.feature.application.presentation.components.FileUploadErrorDialog
import org.getscol.gscol.feature.application.presentation.components.WithdrawApplicationBottomSheet
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

    if(state.showDocumentUploadError){
        state.uploadState?.let {
            FileUploadErrorDialog(uploadState = it){
                action(ApplicationDetailAction.OnCloseErrorUploadDialog)
            }
        }
    }

    WithdrawApplicationBottomSheet(showBottomSheet = state.showConsultantBottomSheet){
        action(ApplicationDetailAction.OnHideWithdrawBottomSheet)
    }

    BaseScreen(
        title = "Application Tracker",
        showLoader = state.isLoading,
        onBackPress = { navigator.navigateBack() }) { paddingValues ->
        ApplicationDetailsScreen(state,action)
    }

}

@Preview
@Composable
fun ApplicationDetailsScreen(
    state: ApplicationDetailsUiState,
    action: (ApplicationDetailAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DocumentTheme.Background)
            .verticalScroll(rememberScrollState())
    ) {

        UniversityHeroHeader(
            imageUrl = state.universityCoverImageUrl.orEmpty(),
            universityName = state.universityName.orEmpty()
        )

        ApplicationInfoCard(
            intake = "${state.intakeMonth ?: ""} ${state.intakeYear}",
            program = state.courseName.orEmpty(),
            applicationId = state.applicationSerialNumber.orEmpty(),
            action = action
        )

        DocumentsSection(documentCheckList = state.documentCheckLists, action)

        ApplicationActionButtons({
            action(ApplicationDetailAction.OnWithdrawApplication)
        }, {
            action(ApplicationDetailAction.OnTrackApplication)
        })
    }
}
