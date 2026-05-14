import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.helper.orFalse
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.ApiResponseBottomSheet
import org.getscol.gscol.core.presentation.components.MaterialDropdown
import org.getscol.gscol.core.presentation.components.PrimaryButton
import org.getscol.gscol.feature.academic_form.presentation.DropDownUiModel
import org.getscol.gscol.feature.application.presentation.application_form.ApplicationFormScreenAction
import org.getscol.gscol.feature.application.presentation.application_form.ApplicationFormUIEffect
import org.getscol.gscol.feature.application.presentation.application_form.ApplicationFormUiState
import org.getscol.gscol.feature.application.presentation.application_form.ApplicationFormViewmodel
import org.getscol.gscol.feature.application.presentation.components.FormField
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun ApplicationFormScreenRoute(
    courseDetails: CourseDetails,
    navigator: Navigator,
    viewmodel: ApplicationFormViewmodel = koinViewModel(
        key = "ApplicationForm-$courseDetails",
        parameters = { parametersOf(courseDetails) }
    )
) {
    val state by viewmodel.applicationFormUiState.collectAsState()
    val action = viewmodel::action

    ObserveEffect(viewmodel.applicationFormUiEffect) { effect ->
        when (effect) {
            is ApplicationFormUIEffect.NavigateToApplicationJourney -> {
                navigator.navigateTo(route = Route.Application(effect.applicationId), true)
            }
        }
    }


    BaseScreen(title = "Applications Form", showLoader = state.isLoading) {
        AddApplicationContent(state, action)
    }

    ApiResponseBottomSheet(
        showBottomSheet = state.showApiResponseBottomSheet.orFalse(),
        isSuccess = state.isApiSuccess.orFalse(),
        message = if (state.isApiSuccess) state.successMsg else state.errorMsg,
        onDismiss = {
            if (state.isApiSuccess) {
                action(ApplicationFormScreenAction.OnNavigateToApplicationJourney)
            }
            action(ApplicationFormScreenAction.OnDismissApiResponseSheet)
        },
        onConfirm = {
            if (state.isApiSuccess) {
                action(ApplicationFormScreenAction.OnNavigateToApplicationJourney)
            }
            action(ApplicationFormScreenAction.OnDismissApiResponseSheet)
        }
    )
}

@Composable
fun AddApplicationContent(
    state: ApplicationFormUiState?,
    action: (ApplicationFormScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {

    var intakeExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // University
        FormField(
            label = "University",
            value = state?.universityName.orEmpty(),
        )

        // Program
        FormField(
            label = "Program",
            value = state?.courseName.orEmpty(),
        )

        // Intake
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = "Intake",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF666666),
            )
            MaterialDropdown(
                selectedValue = state?.selectedIntake.orEmpty(),
                placeholder = "Select Intake",
                options = state?.intakes?.map { DropDownUiModel(itemName = it) }.orEmpty(),
                expanded = intakeExpanded,
                onExpandedChange = { intakeExpanded = it },
                onOptionSelected = {
                    val intakeName = it.itemName
                    if (intakeName != null) {
                        action(ApplicationFormScreenAction.OnIntakeSelection(intakeName))
                    }
                },
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Submit button
        PrimaryButton(
            text = "Create Application",
            onClick = { action(ApplicationFormScreenAction.OnCreateApplication)},
            modifier = Modifier.padding(bottom = 10.dp),
        )
    }
}