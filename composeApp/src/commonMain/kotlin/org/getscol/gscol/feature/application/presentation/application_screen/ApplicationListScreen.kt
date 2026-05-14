package org.getscol.gscol.feature.application.presentation.application_screen

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.helper.ObserveEffect
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.EmptyView
import org.getscol.gscol.core.presentation.components.PullToRefreshIndicator
import org.getscol.gscol.feature.application.domain.model.response.ApplicationInfo
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItem
import org.getscol.gscol.feature.application.presentation.components.ApplicationRowItemShimmer
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.koin.compose.viewmodel.koinViewModel
import kotlin.math.sqrt

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

    val refreshState = rememberPullToRefreshState()

    // Memoize the content padding to avoid recalculation on every recomposition
    val contentPadding = remember(paddingValues) {
        PaddingValues(top = 8.dp, bottom = paddingValues.calculateBottomPadding() + 100.dp)
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = { action(ApplicationListAction.OnRefreshApplicationList) },
        indicator = {
            PullToRefreshIndicator(refreshState = refreshState, isRefreshing = isRefreshing)
        }
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