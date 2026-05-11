package org.getscol.gscol.core.presentation.course

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import org.getscol.gscol.core.helper.toDollar
import org.getscol.gscol.core.helper.toShortDate
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.feature.home.presentation.components.CourseInfoCard
import org.getscol.gscol.feature.home.presentation.components.NoCoursesFound
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route

@Composable
fun CourseItemView(
    navigator: Navigator,
    action: (HomeAction) -> Unit,
    courses: LazyPagingItems<Course>,
    values: PaddingValues,
    isUsedForTopLevelScreen: Boolean = false
) {
    when (val refreshState = courses.loadState.refresh) {
        is LoadState.Loading -> {
            FullScreenLoader()
        }

        is LoadState.Error -> {
            FullScreenError(
                message = refreshState.error.message ?: "Failed to load",
                onRetry = { courses.retry() }
            )
        }

        is LoadState.NotLoading -> {

            if (courses.itemCount <= 0) {
                NoCoursesFound(
                    onContactConsultant = {
                        // navigate or launch intent
                    }
                )

            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = if (isUsedForTopLevelScreen) PaddingValues(bottom = values.calculateBottomPadding() + 80.dp) else PaddingValues(
                        bottom = 0.dp
                    )
                ) {
                    items(count = courses.itemCount) { index ->
                        courses[index]?.let {
                            CourseInfoCard(
                                courseId = it.courseId,
                                courseName = it.courseName,
                                city = it.city,
                                country = it.country,
                                universityName = it.universityName,
                                universityLogo = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg" /*it.imageUrl*/,
                                backgroundImage = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg" /*it.imageUrl*/,
                                intake = it.intake.toShortDate(),
                                tuitionFees = it.tuitionFee.toString().toDollar(),
                                duration = it.duration,
                                scholarship = it.scholarship.toString(),
                                initialDeposit = "12333",
                                ieltsBand = it.ieltsOverallRequired,
                                ieltsSingleBand = it.ieltsBandRequired,
                                isFavorite = it.isWishlisted,
                                action = action,
                                onCourseClick = {
                                    navigator.navigateToOtherScreen(
                                        route = Route.CourseDetails(
                                            courseId = it.courseId,
                                        )
                                    )
                                }
                            )
                        }
                        // Add divider after each item except the last
                        if (index < courses.itemCount - 1) {
                            HorizontalDivider(
                                thickness = 9.dp,
                                color = Color(0xFFE8E8E8)
                            )
                        }
                    }

                    when (courses.loadState.append) {
                        is LoadState.Loading -> {
                            item { PaginationLoader() }
                        }

                        is LoadState.Error -> {
                            item { PaginationError(onRetry = { courses.retry() }) }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }
}

@Composable
fun CourseItemView(
    navigator: Navigator,
    action: (HomeAction) -> Unit,
    courses: List<Course>,
    isLoading: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    values: PaddingValues,
    isUsedForTopLevelScreen: Boolean = false
) {
    when {
        isLoading -> FullScreenLoader()
        errorMessage != null -> FullScreenError(
            message = errorMessage,
            onRetry = onRetry
        )

        courses.isEmpty() -> {
            NoCoursesFound(
                onContactConsultant = {
                }
            )
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = if (isUsedForTopLevelScreen) PaddingValues(bottom = values.calculateBottomPadding() + 80.dp) else PaddingValues(
                    bottom = 0.dp
                )
            ) {
                items(
                    count = courses.size,
                    key = { courses[it].courseId }
                ) { index ->
                    courses[index].let {
                        CourseInfoCard(
                            courseId = it.courseId,
                            courseName = it.courseName,
                            city = it.city,
                            country = it.country,
                            universityName = it.universityName,
                            universityLogo = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg" /*it.imageUrl*/,
                            backgroundImage = "https://images.pexels.com/photos/12610210/pexels-photo-12610210.jpeg" /*it.imageUrl*/,
                            intake = it.intake.toShortDate(),
                            tuitionFees = it.tuitionFee.toString().toDollar(),
                            duration = it.duration,
                            scholarship = it.scholarship.toString(),
                            initialDeposit = "12333",
                            ieltsBand = it.ieltsOverallRequired,
                            ieltsSingleBand = it.ieltsBandRequired,
                            isFavorite = it.isWishlisted,
                            action = action,
                            onCourseClick = {
                                navigator.navigateToOtherScreen(
                                    route = Route.CourseDetails(
                                        courseId = it.courseId,
                                    )
                                )
                            }
                        )
                    }
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
}
