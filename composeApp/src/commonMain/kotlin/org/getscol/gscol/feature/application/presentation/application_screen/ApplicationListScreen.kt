package org.getscol.gscol.feature.application.presentation.application_screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.EmptyView
import org.getscol.gscol.core.presentation.components.ScolPullToRefreshBox
import org.getscol.gscol.feature.application.domain.model.response.ApplicationInfo
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItem
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItemShimmer
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ApplicationListScreenRoute(
    navigator: Navigator,
    viewmodel: ApplicationListViewmodel = koinViewModel()
) {

    val state by viewmodel.applicationListUiState.collectAsState()
    val action = viewmodel::onAction

    ObserveEffect(viewmodel.applicationListUiEffect) { effect ->
        when (effect) {
            is ApplicationListUiEffect.NavigateToApplicationScreen -> {
                navigator.navigateTo(Route.Application(effect.applicationId))
            }
        }
    }

    BaseScreen(
        title = "Applications",
        isTopLevelScreen = true,
        showBackButton = false,
        onBackPress = { navigator.navigateBack() }) {
        ApplicationTrackerContent(
            state.applications.orEmpty(),
            action,
            state.isRefreshing,
            state.isLoading,
            state.showEmptyView,
            it
        )
    }
}

@Composable
fun ApplicationTrackerContent(
    applications: List<ApplicationInfo>,
    action: (ApplicationListAction) -> Unit,
    isRefreshing: Boolean,
    isLoading: Boolean,
    isEmpty: Boolean = false,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {

    if (isEmpty) {
        EmptyView(message = "You have no active university applications.", paddingValues = PaddingValues())
        return
    }

    val contentPadding = remember(paddingValues) {
        PaddingValues(top = 8.dp, bottom = paddingValues.calculateBottomPadding() + 100.dp)
    }

    ScolPullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { action(ApplicationListAction.OnRefreshApplicationList) },
    ) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = contentPadding
        ) {
            if (isLoading) {
                items(3) { ApplicationRowItemShimmer() }
            } else {
                items(
                    items = applications,
                    key = { it.applicationId.orEmpty() }
                ) { application ->
                    ApplicationRowItem(item = application, action = action)
                }
            }
        }
    }
}
