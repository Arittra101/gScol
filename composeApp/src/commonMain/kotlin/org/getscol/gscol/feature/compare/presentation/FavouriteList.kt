package org.getscol.gscol.feature.compare.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.helper.toShortDate
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.feature.home.presentation.components.CourseInfoCard
import org.getscol.gscol.feature.home.presentation.components.heroImageForCard
import org.getscol.gscol.feature.home.presentation.components.universityLogoForCard
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route

@Composable
fun FavouriteList(
    navigator: Navigator,
    action: (HomeAction) -> Unit,
    courses: List<Course>,
    contentPadding: PaddingValues,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(
            count = courses.size,
            key = { courses[it].courseId }
        ) { index ->
            val course = courses[index]
            Column {
                CourseInfoCard(
                    courseId = course.courseId,
                    courseName = course.courseName,
                    city = course.city,
                    country = course.country,
                    universityName = course.universityName,
                    universityLogo = course.universityLogoForCard(),
                    backgroundImage = course.heroImageForCard(),
                    intake = course.intake.toShortDate(),
                    tuitionFees = course.tuitionFee,
                    duration = course.duration,
                    scholarship = course.scholarship,
                    initialDeposit = course.deposit,
                    ieltsBand = course.ieltsOverallRequired,
                    ieltsSingleBand = course.ieltsBandRequired,
                    isFavorite = course.isWishListed,
                    action = action,
                    onCourseClick = {
                        navigator.navigateTo(route = Route.CourseDetails(courseId = course.courseId))
                    }
                )
                if (index < courses.size - 1) {
                    HorizontalDivider(
                        thickness = 9.dp,
                        color = Color(0xFFE8E8E8)
                    )
                }
            }
        }
    }
}
