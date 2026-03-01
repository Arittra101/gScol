package org.getscol.gscol.feature.search.presentation.search

sealed interface SearchAction {
    data class QueryChanged(val query: String) : SearchAction
    data object SearchSubmitted : SearchAction
    data class RemoveHistoryItem(val keyword: String) : SearchAction
    data object LoadHistory : SearchAction
}