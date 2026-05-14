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
import org.getscol.gscol.core.presentation.course.FullScreenError
import org.getscol.gscol.feature.compare.presentation.components.WishlistEmptyState
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CompareScreenRoot(navigator: Navigator) {
    val viewModel: CompareViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val compareState = uiState
    val wishlistOverlayBusy =
        compareState is CompareUiState.Content && compareState.isWishlistMutating
    val initialOrMutationLoader =
        uiState is CompareUiState.Loading || wishlistOverlayBusy

    BaseScreen(
        title = "Wishlist",
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
                FullScreenError(
                    message = state.message,
                    onRetry = viewModel::loadWishlists
                )
            }

            is CompareUiState.Content -> {
                if (state.courses.isEmpty()) {
                    WishlistEmptyState()
                } else {
                    CompareWishlistList(
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
