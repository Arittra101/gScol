package org.getscol.gscol.feature.compare.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompareScreenRoot(navigator: Navigator) {
    val viewModel: CompareViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val compareState = uiState
    val wishlistOverlayBusy =
        compareState is CompareUiState.Content && compareState.isWishlistMutating

    BaseScreen(
        title = "Wishlist",
        showBackButton = false,
        isTopLevelScreen = true,
        showLoader = wishlistOverlayBusy
    ) { paddingValues ->
        when (val state = uiState) {
            is CompareUiState.Loading -> CourseItemView(
                navigator = navigator,
                action = viewModel::onAction,
                courses = emptyList(),
                isLoading = true,
                errorMessage = null,
                onRetry = viewModel::loadWishlists,
                values = paddingValues,
                isUsedForTopLevelScreen = true
            )

            is CompareUiState.Error -> CourseItemView(
                navigator = navigator,
                action = viewModel::onAction,
                courses = emptyList(),
                isLoading = false,
                errorMessage = state.message,
                onRetry = viewModel::loadWishlists,
                values = paddingValues,
                isUsedForTopLevelScreen = true
            )

            is CompareUiState.Content -> CourseItemView(
                navigator = navigator,
                action = viewModel::onAction,
                courses = state.courses,
                isLoading = false,
                errorMessage = null,
                onRetry = viewModel::loadWishlists,
                values = paddingValues,
                isUsedForTopLevelScreen = true
            )
        }
    }
}
