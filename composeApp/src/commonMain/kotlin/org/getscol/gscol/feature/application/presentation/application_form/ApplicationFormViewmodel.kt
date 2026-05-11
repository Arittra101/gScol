package org.getscol.gscol.feature.application.presentation.application_form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.helper.toMonthNumber
import org.getscol.gscol.core.utils.DateTimeFormat
import org.getscol.gscol.core.utils.DateTimeProvider
import org.getscol.gscol.feature.application.data.repository.ApplicationRepository
import org.getscol.gscol.feature.application.domain.model.request.ApplicationCreateRequestBody
import org.getscol.gscol.feature.application.domain.model.request.IntakeRequest
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class ApplicationFormViewmodel(
    private val courseDetails: CourseDetails,
    private val repository: ApplicationRepository,
    private val session: Session
) : ViewModel() {

    private val _applicationFormUiState = MutableStateFlow(ApplicationFormUiState())
    val applicationFormUiState: StateFlow<ApplicationFormUiState> = _applicationFormUiState

    private val _applicationFormUiEffect = Channel<ApplicationFormUIEffect>(Channel.BUFFERED)
    val applicationFormUiEffect = _applicationFormUiEffect.receiveAsFlow()

    private var applicationId: String? = null

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
                    _applicationFormUiState.value.copy(selectedIntake = action.intake)
            }

            is ApplicationFormScreenAction.OnCreateApplication -> {
                createApplication()
            }

            is ApplicationFormScreenAction.OnNavigateToApplicationJourney -> {
                viewModelScope.launch {
                    _applicationFormUiEffect.send(
                        ApplicationFormUIEffect.NavigateToApplicationJourney(
                            applicationId.orEmpty()
                        )
                    )
                }
            }
            is ApplicationFormScreenAction.OnDismissApiResponseSheet -> {
                _applicationFormUiState.value = _applicationFormUiState.value.copy(showApiResponseBottomSheet = false)
            }
        }
    }


    private fun createApplication() {
        val currentYear = DateTimeProvider.now(DateTimeFormat.YearOnly).toIntOrNull() ?: 0

        val requestBody = ApplicationCreateRequestBody(
            universityId = courseDetails.university.uniId,
            courseId = courseDetails.courseId,
            intake = IntakeRequest(
                intakeMonth = _applicationFormUiState.value.selectedIntake?.toMonthNumber(),
                intakeYear = currentYear
            )
        )

        viewModelScope.launch {
            repository.createApplication(requestBody)
                .onStart {
                    _applicationFormUiState.value = _applicationFormUiState.value.copy(isLoading = true)
                }.collect {result ->
                    when(result) {
                        is Result.Success -> {
                            _applicationFormUiState.value = _applicationFormUiState.value.copy(
                                isLoading = false,
                                showApiResponseBottomSheet = true,
                                isApiSuccess = true
                            )
                            applicationId = result.data
                            session.applicationApplyTrigger()
                        }

                        is Result.Error -> {
                            _applicationFormUiState.value = _applicationFormUiState.value.copy(
                                isLoading = false,
                                showApiResponseBottomSheet = true,
                                isApiSuccess = false
                            )
                        }
                    }
                }
        }
    }

}

data class ApplicationFormUiState(
    val universityName: String = "",
    val courseName: String = "",
    val intakes: List<String> = listOf(),
    val selectedIntake: String? = null,
    val isLoading: Boolean = false,
    val showApiResponseBottomSheet: Boolean = false,
    val isApiSuccess: Boolean = false,
    val successMsg: String = "Application created successfully",
    val errorMsg: String = "Something went wrong"
)

sealed interface ApplicationFormUIEffect {
    data class NavigateToApplicationJourney(val applicationId: String) : ApplicationFormUIEffect
}

sealed interface ApplicationFormScreenAction {
    data class OnIntakeSelection(val intake: String) : ApplicationFormScreenAction
    data object OnCreateApplication : ApplicationFormScreenAction
    data object OnNavigateToApplicationJourney : ApplicationFormScreenAction
    data object OnDismissApiResponseSheet : ApplicationFormScreenAction

}
