package org.getscol.gscol.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun InEligibleScreenRoute(
    viewmodel: InEligibleCourseViewmodel = koinViewModel(),
    navigator: Navigator
) {

    val action = viewmodel::onAction
    val wishlistUi by viewmodel.wishlistMutationUiState.collectAsState()
    BaseScreen(
        title = "Ineligible Courses",
        onBackPress = { navigator.navigateBack() },
        showLoader = wishlistUi.isMutating
    ) {
        CourseItemView(
            navigator,
            action,
            viewmodel.courses.collectAsLazyPagingItems(),
            it,
            isIneligibleScreen = true
        )
    }
}