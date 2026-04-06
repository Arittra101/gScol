package org.getscol.gscol.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.core.utils.AppLogger
import org.getscol.gscol.feature.home.presentation.components.HomeAppBar
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewmodel = koinViewModel(),
    navigator: Navigator
) {

    // Instead of the function reference...
    /*  val action: (HomeAction) -> Unit = { data ->
          viewmodel.onAction(data)
      }*/
    val action: (HomeAction) -> Unit = { data ->
        if (data is HomeAction.OnCourseClick) {
            AppLogger.d("Course clicked with id : ${data.courseId}")
            navigator.navigateToOtherScreen(route = Route.CourseDetails(courseId = data.courseId))
        } else viewmodel.onAction(data)
    }

    val academicFormSubmitTrigger by viewmodel.session.academicFormSubmitTrigger.collectAsState(initial = 0)
    val isUserLogin by viewmodel.session.isUserLoggedIn.collectAsState(initial = false)
    val isUserFillupAcademicForm: Boolean = academicFormSubmitTrigger > 0

    BaseScreen(topBar = { HomeAppBar(navigator = navigator, action, isUserFillupAcademicForm, isUserLogin)}, isTopLevelScreen = true) {
        CourseItemView(navigator, action, viewmodel.courses.collectAsLazyPagingItems(), it, true)
    }
}