package org.getscol.gscol.feature.search.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.getscol.gscol.feature.search.presentation.search.SearchAction
import org.getscol.gscol.feature.search.presentation.search.SearchState
import org.getscol.gscol.feature.search.presentation.search.SearchUiEffect
import org.getscol.gscol.feature.search.presentation.search.SearchViewModel
import org.getscol.gscol.navigation.Navigator
import org.getscol.gscol.navigation.Route
import org.getscol.gscol.theme.appColors
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SearchScreenRoot(
    navigator: Navigator,
    viewModel: SearchViewModel = koinViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.onAction(SearchAction.LoadHistory)
    }
    LaunchedEffect(Unit) {
        viewModel.uiEffect.collect { effect ->
            when (effect) {
                is SearchUiEffect.NavigateToResults -> {
                    navigator.navigateToRoute(
                        Route.SearchResults(
                            searchText = effect.searchText,
                            listType = "ELIGIBLE_ONLY"
                        )
                    )
                }
            }
        }
    }
    val state by viewModel.state.collectAsState()

    SearchScreen(
        state = state,
        navigator = navigator,
        onAction = viewModel::onAction
    )
}

@Composable
fun SearchScreen(
    state: SearchState,
    navigator: Navigator,
    onAction: (SearchAction) -> Unit
) {
    val colors = appColors()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.customBackground)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = { navigator.navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            SearchBar(
                query = state.query,
                onQueryChange = { onAction(SearchAction.QueryChanged(it)) },
                onSearch = {
                    focusManager.clearFocus()
                    onAction(SearchAction.SearchSubmitted)
                },
                modifier = Modifier.weight(1f)
            )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Recent Searches",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = colors.customPrimaryText
                )
                Button(
                    modifier = Modifier.height(30.dp),
                    onClick = {
                        navigator.navigateToRoute(Route.AdvancedSearch)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.customPrimary.copy(alpha = 0.15f)
                    ),
                    shape = RoundedCornerShape(18.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)
                ) {
                    Text(
                        "Advance Search",
                        style = MaterialTheme.typography.labelMedium,
                        color = colors.customPrimary,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth().weight(1f, fill = false)
            ) {
                items(state.searchHistory) { keyword ->
                    RecentSearchItem(
                        keyword = keyword,
                        onRemove = { onAction(SearchAction.RemoveHistoryItem(keyword)) },
                        onClick = {
                            onAction(SearchAction.QueryChanged(keyword))
                            onAction(SearchAction.SearchSubmitted)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = appColors()
    Row(
        modifier = modifier
            .height(44.dp)
            .background(
                color = colors.customPrimaryContainer,
                shape = RoundedCornerShape(50.dp)
            )
            .border(
                width = 1.dp,
                color = colors.customInfo,
                shape = RoundedCornerShape(50.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                fontSize = 14.sp,
                color = colors.customPrimaryText
            ),
            modifier = Modifier.weight(1f),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { onSearch() }),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        "Search country, course, intake",
                        fontSize = 13.sp,
                        color = colors.customInfo
                    )
                }
                innerTextField()
            }
        )
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            Icons.Default.Search,
            contentDescription = null,
            tint = colors.customInfo,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun RecentSearchItem(
    keyword: String,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    val colors = appColors()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = colors.customSurface,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.AccessTime,
            contentDescription = null,
            tint = colors.customPrimary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = keyword,
            style = MaterialTheme.typography.bodyMedium,
            color = colors.customPrimaryText,
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = onRemove,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                tint = colors.customPrimary,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}
