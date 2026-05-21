package org.getscol.gscol.feature.search.presentation.search_result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultsScreenRoot(
    searchText: String,
    advancedParams: AdvancedSearchParams?,
    navigator: Navigator,
) {
    val viewModel: SearchResultsViewModel = koinViewModel(
        key = "SearchResults-$searchText-${advancedParams != null}"
    )
    LaunchedEffect(searchText, advancedParams) {
        viewModel.setParams(searchText, advancedParams)
    }

    val courses = viewModel.courses.collectAsLazyPagingItems()
    val action = viewModel::onAction
    val wishlistUi by viewModel.wishlistMutationUiState.collectAsState()

    BaseScreen(
        title = "Search Result",
        onBackPress = { navigator.navigateBack() },
        showLoader = wishlistUi.isMutating
    ) {
        CourseItemView(navigator, action, courses, it, isSearchResultScreen = true)
    }
}