package org.getscol.gscol.core.presentation.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * App-wide pull-to-refresh: Material3 [PullToRefreshBox] plus [PullToRefreshIndicator].
 * Use inside [org.getscol.gscol.core.presentation.BaseScreen] content for scrollable lists/feeds.
 */
@Composable
fun ScolPullToRefreshBox(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val refreshState = rememberPullToRefreshState()
    PullToRefreshBox(
        modifier = modifier.fillMaxSize(),
        state = refreshState,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        indicator = {
            PullToRefreshIndicator(refreshState = refreshState, isRefreshing = isRefreshing)
        },
    ) {
        content()
    }
}
