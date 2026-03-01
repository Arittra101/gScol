package org.getscol.gscol.feature.search.presentation.search_result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.feature.home.domain.model.Course
import org.getscol.gscol.feature.home.presentation.HomeAction
import org.getscol.gscol.feature.home.presentation.components.CourseInfoCard
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchResultsScreenRoot(
    searchText: String,
    listType: String,
    advancedParams: AdvancedSearchParams?,
    navigator: Navigator
) {
    val viewModel: SearchResultsViewModel = koinViewModel(
        key = "SearchResults-$searchText-$listType-${advancedParams != null}"
    )
    LaunchedEffect(searchText, listType, advancedParams) {
        viewModel.setParams(searchText, listType, advancedParams)
    }
    val courses = viewModel.courses.collectAsLazyPagingItems()
    SearchResultsScreen(
        courses = courses,
        navigator = navigator,
        onRetry = { courses.retry() }
    )
}

@Composable
fun SearchResultsScreen(
    courses: LazyPagingItems<Course>,
    navigator: Navigator,
    onRetry: () -> Unit,
    onHomeAction: (HomeAction) -> Unit = {}
) {
    val colors = appColors()
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colors.customSecondaryContainer)
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(
                    modifier = Modifier.background(color = Color.Transparent),
                    onClick = { navigator.navigateBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Search Results",
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.customPrimaryText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(colors.customBackground)
        ) {
            when (val refreshState = courses.loadState.refresh) {
                is LoadState.Loading -> FullScreenLoader()
                is LoadState.Error -> FullScreenError(
                    message = refreshState.error.message ?: "Failed to load",
                    onRetry = onRetry
                )

                else -> {
                    LazyColumn(
                        modifier = Modifier.navigationBarsPadding(),
                        contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        items(count = courses.itemCount) { index ->
                            courses[index]?.let { course ->
                                CourseInfoCard(
                                    courseId = course.courseId,
                                    courseName = course.courseName,
                                    city = course.city,
                                    country = course.country,
                                    universityName = course.universityName,
                                    universityLogo = course.imageUrl,
                                    backgroundImage = course.imageUrl,
                                    intake = course.intake,
                                    tuitionFees = course.tuitionFee.toString(),
                                    duration = course.duration,
                                    scholarship = course.scholarship.toString(),
                                    initialDeposit = course.deposit.toString(),
                                    ieltsBand = course.ieltsOverallRequired,
                                    ieltsSingleBand = course.ieltsBandRequired,
                                    isFavorite = course.isWishlisted,
                                    action = onHomeAction
                                )
                            }
                        }
                        when (courses.loadState.append) {
                            is LoadState.Loading -> item { PaginationLoader() }
                            is LoadState.Error -> item {
                                PaginationError(onRetry = onRetry)
                            }

                            else -> Unit
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun FullScreenError(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun PaginationLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun PaginationError(onRetry: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onRetry) { Text("Retry") }
    }
}
