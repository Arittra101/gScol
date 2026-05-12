package org.getscol.gscol.core.presentation.course

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

// Memoize the divider color to avoid recreation on every frame
private val DIVIDER_COLOR = Color(0xFFE8E8E8)

// Memoize default bottom padding to avoid recreation
private val DEFAULT_PADDING = PaddingValues(bottom = 0.dp)

@Composable
fun CourseItemView(
    navigator: Navigator,
    action: (HomeAction) -> Unit,
    courses: LazyPagingItems<Course>,
    values: PaddingValues,
    isUsedForTopLevelScreen: Boolean = false
) {
    // Simple conditional - don't need remember for lightweight operation
    val contentPadding = if (isUsedForTopLevelScreen) {
        PaddingValues(bottom = values.calculateBottomPadding() + 80.dp)
    } else {
        DEFAULT_PADDING
    }

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
                    contentPadding = contentPadding
                ) {
                    items(
                        count = courses.itemCount,
                        key = { index ->
                            val courseId = courses.peek(index)?.courseId
                            if (courseId != null) "$courseId-$index"
                            else "placeholder_$index"
                        }) { index ->
                        courses[index]?.let { course ->
                            // Only memoize expensive callback creation
                            val onCourseClick: () -> Unit = remember(course.courseId, navigator) {
                                {
                                    navigator.navigateToOtherScreen(
                                        route = Route.CourseDetails(courseId = course.courseId)
                                    )
                                }
                            }

                            CourseInfoCard(
                                courseId = course.courseId,
                                courseName = course.courseName,
                                city = course.city,
                                country = course.country,
                                universityName = course.universityName,
                                universityLogo = course.imageUrl,
                                backgroundImage = course.imageUrl,
                                intake = course.intake.toShortDate(),
                                tuitionFees = course.tuitionFee.toString().toDollar(),
                                duration = course.duration,
                                scholarship = course.scholarship.toString(),
                                initialDeposit = "12333",
                                ieltsBand = course.ieltsOverallRequired,
                                ieltsSingleBand = course.ieltsBandRequired,
                                isFavorite = course.isWishlisted,
                                action = action,
                                onCourseClick = onCourseClick
                            )
                        }
                        // Add divider after each item except the last
                        if (index < courses.itemCount - 1) {
                            HorizontalDivider(
                                thickness = 9.dp,
                                color = DIVIDER_COLOR
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
