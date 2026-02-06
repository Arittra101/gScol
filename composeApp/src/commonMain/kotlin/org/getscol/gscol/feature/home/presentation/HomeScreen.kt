package org.getscol.gscol.feature.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import app.cash.paging.compose.collectAsLazyPagingItems
import org.getscol.gscol.core.Helper.toDollar
import org.getscol.gscol.core.Helper.toShortDate
import org.getscol.gscol.feature.home.presentation.components.CourseInfoCard
import org.getscol.gscol.feature.home.presentation.components.HomeAppBar
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun HomeScreenRoot(
    viewmode: HomeViewmodel = koinViewModel(),
    navigator: Navigator
) {

    val action = viewmode::onAction
    // Instead of the function reference...
    /*  val action: (HomeAction) -> Unit = { data ->
          viewmodel.onAction(data)
      }*/

    HomeScreen(viewmode,navigator,action)
}

@Composable
fun HomeScreen(
    viewModel: HomeViewmodel,
    navigator: Navigator,
    action: (HomeAction) -> Unit
) {
    val courses = viewModel.courses.collectAsLazyPagingItems()

    Scaffold(
        topBar = { HomeAppBar(navigator = navigator,action) },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(appColors().customBackground)
        ) {

            when (val refreshState = courses.loadState.refresh) {
                is LoadState.Loading -> { FullScreenLoader() }

                is LoadState.Error -> {
                    FullScreenError(
                        message = refreshState.error.message ?: "Failed to load",
                        onRetry = { courses.retry() }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.navigationBarsPadding(),
                        contentPadding = PaddingValues(
                            bottom = innerPadding.calculateTopPadding()
                        )
                    ) {

                        items(count = courses.itemCount) { index ->
                            courses[index]?.let {
                                CourseInfoCard(
                                    courseId = it.courseId,
                                    courseName = it.courseName,
                                    city = it.city,
                                    country = it.country,
                                    universityName = it.universityName,
                                    universityLogo = it.imageUrl,
                                    backgroundImage = it.imageUrl,
                                    intake = it.intake.toShortDate(),
                                    tuitionFees = it.tuitionFee.toString().toDollar(),
                                    duration = it.duration,
                                    scholarship = it.scholarship.toString(),
                                    initialDeposit = "12333",
                                    ieltsBand = it.ieltsOverallRequired,
                                    ieltsSingleBand = it.ieltsBandRequired,
                                    isFavorite = it.isWishlisted,
                                    action = action
                                )
                            }
                        }

                        when (courses.loadState.append) {
                            is LoadState.Loading -> {
                                item { PaginationLoader() }
                            }
                            is LoadState.Error -> {
                                item { PaginationError(onRetry = { courses.retry() }) }
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
fun PaginationLoader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) { CircularProgressIndicator() }
}


@Composable
fun FullScreenError(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message)
            Spacer(modifier = Modifier.height(12.dp))
            Button(onClick = onRetry) {
                Text("Retry")
            }
        }
    }
}


@Composable
fun PaginationError(
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        contentAlignment = Alignment.Center
    ) { Button(onClick = onRetry) { Text("Retry") } }
}

