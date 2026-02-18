package org.getscol.gscol.feature.search.presentation.search

data class SearchState(
    val query: String = "",
    val searchHistory: List<String> = emptyList()
)