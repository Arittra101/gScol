package org.getscol.gscol.feature.home.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.ExitConfirmationDialog
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.core.utils.closeApp
import org.getscol.gscol.feature.home.presentation.components.HomeAppBar
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun HomeScreenRoot(
    viewmodel: HomeViewmodel = koinViewModel(),
    navigator: Navigator
) {

    val action: (HomeAction) -> Unit = { data ->
        viewmodel.onAction(data)
    }

    val academicFormSubmitTrigger by viewmodel.session.academicFormSubmitTrigger.collectAsState(
        initial = 0
    )

    val isUserLogin by viewmodel.session.isUserLoggedIn.collectAsState(initial = false)
    val isUserFillupAcademicForm: Boolean = academicFormSubmitTrigger > 0
    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler { showExitDialog = true }

    val wishlistUi by viewmodel.wishlistMutationUiState.collectAsState()

    BaseScreen(topBar = {
        HomeAppBar(
            navigator = navigator,
            action,
            isUserFillupAcademicForm,
            isUserLogin
        )
    }, isTopLevelScreen = true, showLoader = wishlistUi.isMutating) {
        CourseItemView(navigator, action, viewmodel.courses.collectAsLazyPagingItems(), it, true)
    }

    if (showExitDialog) {
        ExitConfirmationDialog(onConfirm = { closeApp() }, onDismiss = { showExitDialog = false })
    }
}