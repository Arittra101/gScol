package org.getscol.gscol.feature.compare.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.components.ScolPullToRefreshBox
import org.getscol.gscol.core.presentation.course.FullScreenError
import org.getscol.gscol.feature.compare.presentation.components.WishlistEmptyState
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompareScreenRoot(navigator: Navigator) {
    val viewModel: CompareViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val compareState = uiState
    val wishlistOverlayBusy =
        compareState is CompareUiState.Content && compareState.isWishlistMutating
    val initialOrMutationLoader =
        uiState is CompareUiState.Loading || wishlistOverlayBusy

    BaseScreen(
        title = "Wishlist",
        showBackButton = false,
        isTopLevelScreen = true,
        showLoader = initialOrMutationLoader
    ) { paddingValues ->
        val listBottomPadding = paddingValues.calculateBottomPadding() + 80.dp
        val listContentPadding = PaddingValues(bottom = listBottomPadding)

        when (val state = uiState) {
            is CompareUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize())
            }

            is CompareUiState.Error -> {
                ScolPullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::onRefresh,
                ) {
                    FullScreenError(
                        message = state.message,
                        onRetry = viewModel::loadWishlists
                    )
                }
            }

            is CompareUiState.Content -> {
                ScolPullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = viewModel::onRefresh,
                ) {
                    if (state.courses.isEmpty()) {
                        WishlistEmptyState()
                    } else {
                        FavouriteList(
                            navigator = navigator,
                            action = viewModel::onAction,
                            courses = state.courses,
                            contentPadding = listContentPadding,
                        )
                    }
                }
            }
        }
    }
}
