package org.getscol.gscol.feature.home.presentation

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    // Memoize the action callback to prevent recreation on every recomposition
    val action: (HomeAction) -> Unit = remember(viewmodel) {
        { data -> viewmodel.onAction(data) }
    }

    val listState = rememberLazyListState()
    LaunchedEffect(Unit) {
        viewmodel.scrollResetEvent.collect {
            listState.scrollToItem(0)
        }
    }

    val academicFormSubmitTrigger by viewmodel.session.academicFormSubmitTrigger.collectAsState(
        initial = 0
    )

    val isUserLogin by viewmodel.session.isUserLoggedIn.collectAsState(initial = false)
    val isUserFillupAcademicForm: Boolean = academicFormSubmitTrigger > 0
    var showExitDialog by remember { mutableStateOf(false) }

    // Memoize the back handler callback
    val onBackPressed = remember {
        { showExitDialog = true }
    }

    BackHandler(onBack = onBackPressed)

    val wishlistUi by viewmodel.wishlistMutationUiState.collectAsState()

    BaseScreen(topBar = {
        HomeAppBar(
            navigator = navigator,
            action,
            isUserFillupAcademicForm,
            isUserLogin
        )
    }, isTopLevelScreen = true, showLoader = wishlistUi.isMutating) {
        CourseItemView(
            navigator,
            action,
            viewmodel.courses.collectAsLazyPagingItems(),
            it,
            true,
            listState = listState
        )
    }

    if (showExitDialog) {
        ExitConfirmationDialog(onConfirm = { closeApp() }, onDismiss = { showExitDialog = false })
    }
}