package org.getscol.gscol.feature.search.presentation.search

sealed interface SearchUiEffect {
    data class NavigateToResults(val searchText: String) : SearchUiEffect
}