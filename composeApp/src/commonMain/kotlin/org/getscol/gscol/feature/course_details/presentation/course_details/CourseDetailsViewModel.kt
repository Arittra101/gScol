package org.getscol.gscol.feature.course_details.presentation.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.domain.repository.CourseDetailsRepository

class CourseDetailsViewModel(
    private val repository: CourseDetailsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CourseDetailsState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CourseDetailsUiEffect>(replay = 0)
    val uiEffect = _uiEffect.asSharedFlow()

    fun setCourseId(courseId: String) {
        loadCourseDetails(courseId)
    }

    private fun loadCourseDetails(courseId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            when (val result = repository.getCourseDetails(courseId)) {
                is Result.Success -> {
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            courseDetails = result.data,
                            error = null,
                        )
                    }
                }

                is Result.Error -> {
                    _state.update { s ->
                        s.copy(
                            isLoading = false,
                            error = result.error.name,
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: CourseDetailsAction) {
        when (action) {
            is CourseDetailsAction.TabSelected ->
                _state.update { it.copy(selectedTabIndex = action.index) }

            CourseDetailsAction.ApplyNow ->
                viewModelScope.launch {
                    _uiEffect.emit(
                        CourseDetailsUiEffect.ApplyNow(
                            state.value.courseDetails?.courseId ?: ""
                        )
                    )
                }

            CourseDetailsAction.ReadMoreClicked -> { /* expand about text handled in UI */
            }
        }
    }

}
