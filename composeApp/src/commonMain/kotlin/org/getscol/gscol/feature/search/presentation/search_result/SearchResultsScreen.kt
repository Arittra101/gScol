package org.getscol.gscol.feature.search.presentation.search_result

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.presentation.BaseScreen
import org.getscol.gscol.core.presentation.course.CourseItemView
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.navigation.Navigator
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultsScreenRoot(
    searchText: String,
    listType: String,
    advancedParams: AdvancedSearchParams?,
    navigator: Navigator,
) {
    val viewModel: SearchResultsViewModel = koinViewModel(
        key = "SearchResults-$searchText-$listType-${advancedParams != null}"
    )
    LaunchedEffect(searchText, listType, advancedParams) {
        viewModel.setParams(searchText, listType, advancedParams)
    }

    val courses = viewModel.courses.collectAsLazyPagingItems()
    val action = viewModel::onAction

    BaseScreen(title = "Search Result", onBackPress = { navigator.navigateBack() }) {
        CourseItemView(navigator, action, courses, it)
    }
}