package org.getscol.gscol

import androidx.compose.runtime.Composable
import org.getscol.gscol.feature.course_details.domain.model.AboutUs
import org.getscol.gscol.feature.course_details.domain.model.AcademicRequirements
import org.getscol.gscol.feature.course_details.domain.model.CampusLife
import org.getscol.gscol.feature.course_details.domain.model.Coordinates
import org.getscol.gscol.feature.course_details.domain.model.DegreeRequirement
import org.getscol.gscol.feature.course_details.domain.model.EnglishRequirement
import org.getscol.gscol.feature.course_details.domain.model.CourseDetails
import org.getscol.gscol.feature.course_details.domain.model.CourseLocation
import org.getscol.gscol.feature.course_details.domain.model.CourseRanking
import org.getscol.gscol.feature.course_details.domain.model.CourseTab
import org.getscol.gscol.feature.course_details.domain.model.CourseTag
import org.getscol.gscol.feature.course_details.domain.model.InfoBlock
import org.getscol.gscol.feature.course_details.domain.model.InfoMetaData
import org.getscol.gscol.feature.course_details.domain.model.IntakeDates
import org.getscol.gscol.feature.course_details.domain.model.TuitionFees
import org.getscol.gscol.feature.course_details.domain.model.FeeItems
import org.getscol.gscol.feature.course_details.domain.model.FeesAndScholarships
import org.getscol.gscol.feature.course_details.domain.model.Requirements
import org.getscol.gscol.feature.course_details.domain.model.University
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
    ranking = CourseRanking(position = 42, hasInfo = true, infoKey = "rankingMetaData"),
    university = University(
        uniId = "u1",
        uniName = "University of Leicester",
        uniLogoUrl = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg",
        uniCoverImageUrl = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg",
    ),
    tags = listOf(
        CourseTag(label = "Estd. 1921", type = "established"),
        CourseTag(label = "PUBLIC", type = "type"),
        CourseTag(label = "Leicester, UK", type = "location"),
    ),
    tabs = listOf(
        CourseTab(key = "aboutUs", label = "About Us"),
        CourseTab(key = "campusLife", label = "Campus Life"),
        CourseTab(key = "location", label = "Location"),
        CourseTab(key = "academicRequirements", label = "Academic Info"),
        CourseTab(key = "feesAndScholarships", label = "Fees & Scholarships"),
        CourseTab(key = "intakeDates", label = "Intake Dates"),
    ),
    aboutUs = AboutUs(
        description = listOf(
            "Founded in 1921 as a living memorial to those who lost their lives in the First World War, the University of Leicester is a world-leading research-intensive university. We deliver high-quality education and research that changes the world."
        )
    ),
    campusLife = CampusLife(
        videoUrls = listOf(
            "https://youtu.be/hgiY64CKGUs?si=eQiTzKj88PcKYVra",
            "https://youtu.be/hgiY64CKGUs?si=eQiTzKj88PcKYVra",
        )
    ),
    location = CourseLocation(
        city = "Leicester",
        country = "UK",
        state = "Leicestershire",
        address = "University Road, Leicester LE1 7RH, United Kingdom",
        coordinates = Coordinates(latitude = 52.6196, longitude = -1.1266),
    ),
    academicRequirements = AcademicRequirements(
        hasInfo = true,
        infoKey = "academicRequirementsMetaData",
        requirements = Requirements(
            degreeRequirements = listOf(
                DegreeRequirement(degreeName = "Bachelor's", label = "GPA | CGPA", minValue = "3.5"),
                DegreeRequirement(degreeName = "Master's", label = "GPA | CGPA", minValue = "3.5"),
            ),
            englishRequirements = listOf(
                EnglishRequirement(testName = "IELTS", minOverallValue = "7.0", minSectionValue = "7.0"),
                EnglishRequirement(testName = "TOEFL", minOverallValue = "100", minSectionValue = "20"),
            ),
        ),
    ),
    feesAndScholarships = FeesAndScholarships(
        hasInfo = true,
        infoKey = "feesAndScholarshipsMetaData",
        items = FeeItems(
            tuitionFees = TuitionFees(amount = "45000", currency = "USD", frequency = "yearly"),
            scholarshipsText = "Available",
        ),
    ),
    intakeDates = IntakeDates(
        hasInfo = true,
        infoKey = "intakeDatesMetaData",
        intakeRows = listOf(
            "Fall" to "September",
            "Spring" to "March",
        ),
    ),
    meta = listOf(
        InfoMetaData(
            infoKey = "rankingMetaData",
            title = "Ranking",
            information = listOf(
                InfoBlock(
                    description = listOf(
                        "Ranked #42 globally for International Business Management based on research output, student satisfaction, and graduate employability.",
                        "Consistently ranked in the top 20 universities across the United Kingdom.",
                    )
                )
            ),
        ),
        InfoMetaData(
            infoKey = "academicRequirementsMetaData",
            title = "Academic Requirements",
            information = listOf(
                InfoBlock(
                    subtitle = "GPA",
                    description = listOf(
                        "A minimum GPA of 3.5 out of 4.0 is required for consideration into the program."
                    )
                ),
                InfoBlock(
                    subtitle = "English Proficiency",
                    description = listOf(
                        "Non-native English speakers must submit TOEFL scores of 100+ or IELTS scores of 7.0 or above."
                    )
                ),
            ),
        ),
        InfoMetaData(
            infoKey = "feesAndScholarshipsMetaData",
            title = "Fees & Scholarships",
            information = listOf(
                InfoBlock(
                    subtitle = "Tuition Fees",
                    description = listOf(
                        "The annual tuition fee is $45,000, covering all core modules and university facilities."
                    )
                ),
                InfoBlock(
                    subtitle = "Scholarships",
                    description = listOf(
                        "Merit-based and need-based scholarships are available. Students can apply during the admissions process."
                    )
                ),
            ),
        ),
        InfoMetaData(
            infoKey = "intakeDatesMetaData",
            title = "Intake Dates",
            information = listOf(
                InfoBlock(
                    subtitle = "Fall Intake",
                    description = listOf(
                        "The Fall intake begins in September. Application deadline is typically June 30th."
                    )
                ),
                InfoBlock(
                    subtitle = "Spring Intake",
                    description = listOf(
                        "The Spring intake begins in March. Application deadline is typically December 15th."
                    )
                ),
            ),
        ),
    ),
)
