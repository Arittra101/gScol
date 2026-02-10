package org.getscol.gscol.feature.search.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.feature.search.domain.repository.SearchRepository

class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<SearchUiEffect>(replay = 0)
    val uiEffect: SharedFlow<SearchUiEffect> = _uiEffect.asSharedFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.QueryChanged -> _state.update { it.copy(query = action.query) }
            SearchAction.SearchSubmitted -> submitSearch()
            is SearchAction.RemoveHistoryItem -> removeFromHistory(action.keyword)
            SearchAction.LoadHistory -> loadHistory()
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _state.update { it.copy(searchHistory = searchRepository.getSearchHistory()) }
        }
    }

    private fun submitSearch() {
        val query = _state.value.query.trim()
        if (query.isBlank()) return
        viewModelScope.launch {
            searchRepository.addToSearchHistory(query)
            loadHistory()
            _uiEffect.emit(SearchUiEffect.NavigateToResults(searchText = query))
        }
    }

    private fun removeFromHistory(keyword: String) {
        viewModelScope.launch {
            searchRepository.removeFromSearchHistory(keyword)
            loadHistory()
        }
    }
}