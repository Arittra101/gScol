package org.getscol.gscol

import androidx.compose.runtime.Composable
import org.getscol.gscol.feature.course_details.domain.model.CampusLifeItem
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import org.getscol.gscol.feature.course_details.presentation.course_details.CourseDetailsScreen
import org.getscol.gscol.feature.course_details.presentation.course_details.CourseDetailsState
import org.getscol.gscol.theme.ScolTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ScolTheme {
        CourseDetailsScreen(
            state = CourseDetailsState(
                courseDetails = sampleCourseDetails
            ),
            onAction = {},
            onBack = {}
        )
    }
}

val sampleCourseDetails = CourseDetails(
    courseId = "1",
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
        CampusLifeItem("Student Life", null, "2:17", "15", true),
        CampusLifeItem("Virtual Explore our", null, null, null, false),
    ),
    locationMapPlaceholder = true,
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
