package org.getscol.gscol.feature.course_details.presentation.course_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.feature.course_details.domain.model.CampusLifeItem
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails

class CourseDetailsViewModel : ViewModel() {

    private val _state = MutableStateFlow(CourseDetailsState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<CourseDetailsUiEffect>(replay = 0)
    val uiEffect = _uiEffect.asSharedFlow()

    fun setCourseId(courseId: String) {
        loadCourseDetails(courseId)
    }

    private fun loadCourseDetails(courseId: String) {
        _state.update { it.copy(isLoading = true) }
        // TODO: Replace with repository call when API is available
        viewModelScope.launch {
            _state.update { s ->
                s.copy(
                    isLoading = false,
                    courseDetails = sampleCourseDetails(courseId),
                )
            }
        }
    }

    fun onAction(action: CourseDetailsAction) {
        when (action) {
            is CourseDetailsAction.TabSelected ->
                _state.update { it.copy(selectedTabIndex = action.index) }
            CourseDetailsAction.ApplyNow ->
                viewModelScope.launch {
                    _uiEffect.emit(CourseDetailsUiEffect.ApplyNow(state.value.courseDetails?.courseId ?: ""))
                }
            CourseDetailsAction.ReadMoreClicked -> { /* expand about text handled in UI */ }
        }
    }

    private fun sampleCourseDetails(id: String): CourseDetails = CourseDetails(
        courseId = id,
        courseName = "International Business Management",
        ranking = "#42",
        universityName = "University of Leicester",
        universityLogoUrl = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg",
        imageUrl = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg",
        establishedYear = "Estd. 1921",
        institutionType = "PUBLIC",
        location = "Leicester, UK",
        aboutUs = "Founded in 1921 as a living memorial to those who lost their lives in the First World War, the University of Leicester is a world-leading research-intensive university. We deliver high-quality education and research that changes the world.",
        campusLifeVideos = listOf(
            CampusLifeItem(
                title = "Campus tour",
                thumbnailUrl = null,
                duration = "3:12",
                count = null,
                isVideo = true,
                videoUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
            ),
            CampusLifeItem(
                title = "Student life vlog",
                thumbnailUrl = null,
                duration = "5:45",
                count = null,
                isVideo = true,
                videoUrl = "https://youtu.be/ysz5v6X0K4k",
            ),
        ),
        locationMapPlaceholder = true,
        latitude = 52.6369,
        longitude = -1.1398,
        academicRequirements = listOf(
            "GPA" to "3.5+",
            "English Scores" to "TOEFL: 100+, IELTS: 7.0+",
        ),
        feesAndScholarships = listOf(
            "Tuition Fees" to "$45.000 / year",
            "Scholarships" to "Available",
        ),
        intakeDates = listOf(
            "Fall" to "September",
            "Spring" to "March",
        ),
    )
}
