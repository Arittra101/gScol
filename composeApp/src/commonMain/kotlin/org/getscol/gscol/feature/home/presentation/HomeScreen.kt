package org.getscol.gscol.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.feature.home.presentation.components.HomeAppBar
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewmodel = koinViewModel(),
    navigator: Navigator
) {

    val action = viewmodel::onAction
    // Instead of the function reference...
    /*  val action: (HomeAction) -> Unit = { data ->
          viewmodel.onAction(data)
      }*/

    val academicFormSubmitTrigger by viewmodel.session.academicFormSubmitTrigger.collectAsState(initial = 0)
    val isUserLogin by viewmodel.session.isUserLoggedIn.collectAsState(initial = false)
    val isUserFillupAcademicForm: Boolean = academicFormSubmitTrigger > 0

    BaseScreen(topBar = { HomeAppBar(navigator = navigator, action, isUserFillupAcademicForm, isUserLogin)}, isTopLevelScreen = true) {
        CourseItemView(navigator, action, viewmodel.courses.collectAsLazyPagingItems(), it, false)
    }
}