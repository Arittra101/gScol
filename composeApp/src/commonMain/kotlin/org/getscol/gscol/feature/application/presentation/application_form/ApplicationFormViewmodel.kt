package org.getscol.gscol.feature.application.presentation.application_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.toData
import org.getscol.gscol.core.helper.toMonthNumber
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.request.ApplicationCreateRequestBody
import org.getscol.gscol.feature.application.domain.model.request.IntakeRequest
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ApplicationFormViewmodel(
    private val courseDetails: CourseDetails,
    private val repository: ApplicationRepository
) : ViewModel() {

    private val _applicationFormUiState = MutableStateFlow<ApplicationFormUiState?>(null)
    val applicationFormUiState: StateFlow<ApplicationFormUiState?> = _applicationFormUiState

    private val _applicationFormUiEffect = Channel<ApplicationFormUIEffect>(Channel.BUFFERED)
    val applicationFormUiEffect = _applicationFormUiEffect.receiveAsFlow()

    init {
        _applicationFormUiState.value = ApplicationFormUiState(
            universityName = courseDetails.university.uniName,
            courseName = courseDetails.courseName,
            intakes = courseDetails.intakeDates?.intakes.orEmpty(),
            selectedIntake = null
        )
    }


    fun action(action: ApplicationFormScreenAction) {
        when (action) {
            is ApplicationFormScreenAction.OnIntakeSelection -> {
                _applicationFormUiState.value =
                    _applicationFormUiState.value?.copy(selectedIntake = action.intake)
            }

            ApplicationFormScreenAction.OnCreateApplication -> {
                createApplication()
            }
        }
    }


    private fun createApplication() {
        val currentYear = 2026 //this should be dynamic

        val requestBody = ApplicationCreateRequestBody(
            universityId = courseDetails.university.uniId,
            courseId = courseDetails.courseId,
            intake = IntakeRequest(
                intakeMonth = _applicationFormUiState.value?.selectedIntake?.toMonthNumber(),
                intakeYear = currentYear
            )
        )

        viewModelScope.launch {
            repository.createApplication(requestBody)
                .onStart {
                    _applicationFormUiState.value = _applicationFormUiState.value?.copy(isLoading = true)
                }
                .collect {
                    _applicationFormUiState.value = _applicationFormUiState.value?.copy(isLoading = false)
                    val applicationId = it.toData() ?: return@collect

                    _applicationFormUiEffect.send(ApplicationFormUIEffect.NavigateToApplicationJourney(applicationId))
                }
        }
    }

}

data class ApplicationFormUiState(
    val universityName: String,
    val courseName: String,
    val intakes: List<String>,
    val selectedIntake: String? = null,
    val isLoading: Boolean = false
)

sealed interface ApplicationFormUIEffect {
    data class NavigateToApplicationJourney(val applicationId: String) : ApplicationFormUIEffect
}

sealed interface ApplicationFormScreenAction {
    data class OnIntakeSelection(val intake: String) : ApplicationFormScreenAction
    data object OnCreateApplication : ApplicationFormScreenAction
}
