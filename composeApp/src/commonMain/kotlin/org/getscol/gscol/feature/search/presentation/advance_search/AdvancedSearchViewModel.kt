package org.getscol.gscol.feature.search.presentation.advance_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.core.components.DropdownOption
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.currentYear
import org.getscol.gscol.feature.search.domain.model.AdvancedFilters
import org.getscol.gscol.feature.search.domain.model.AdvancedFlags
import org.getscol.gscol.feature.search.domain.model.AdvancedRanges
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.model.IntakeFilter
import org.getscol.gscol.feature.search.domain.model.MinMax
import org.getscol.gscol.feature.search.domain.repository.SearchRepository

sealed interface AdvancedSearchUiEffect {
    data class NavigateToResults(val searchText: String, val advancedParams: AdvancedSearchParams) : AdvancedSearchUiEffect
}

class AdvancedSearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AdvancedSearchState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AdvancedSearchUiEffect>(replay = 0)
    val uiEffect = _uiEffect.asSharedFlow()

    init {
        _state.update { it.copy(selectedIntakeYear = currentYear()) }
        viewModelScope.launch { loadFilters() }
    }

    private suspend fun loadFilters() {
        when (val result = searchRepository.getAdvancedFilterOptions()) {
            is Result.Success -> {
                val countryOptions = result.data.countryOptions.map { DropdownOption(it.id, it.name) }
                val courseOptions = result.data.programmeOptions.map { DropdownOption(it.id, it.name) }
                _state.update { it.copy(countryOptions = countryOptions, courseOptions = courseOptions, showLoader = false) }
            }
            is Result.Error -> {
                _state.update { it.copy(showLoader = false) }
            }
        }
    }

    private suspend fun loadCities(countryId: String) {
        _state.update { it.copy(showLoader = true) }
        when (val result = searchRepository.getCities(countryId)) {
            is Result.Success -> {
                val cityOptions = result.data.map { DropdownOption(it.id, it.name) }
                _state.update { it.copy(cityOptions = cityOptions, showLoader = false) }
            }
            is Result.Error -> _state.update { it.copy(cityOptions = emptyList()) }
        }
    }

    fun onAction(action: AdvancedSearchAction) {
        when (action) {
            is AdvancedSearchAction.CountrySelected -> {
                val option = action.option
                _state.update {
                    it.copy(
                        selectedCountry = option,
                        selectedCity = null,
                        cityOptions = if (option == null) emptyList() else it.cityOptions
                    )
                }
                if (option != null) {
                    viewModelScope.launch { loadCities(option.id) }
                }
            }
            is AdvancedSearchAction.CitySelected -> _state.update { it.copy(selectedCity = action.option) }
            is AdvancedSearchAction.CourseSelected -> _state.update { it.copy(selectedCourse = action.option) }
            is AdvancedSearchAction.IntakeYearSelected -> _state.update { it.copy(selectedIntakeYear = action.year) }
            is AdvancedSearchAction.IntakeMonthSelected -> _state.update { s ->
                val month = action.month.coerceIn(1, 12)
                when {
                    s.firstSelectedMonth == null ->
                        s.copy(firstSelectedMonth = month, lastSelectedMonth = null)
                    s.lastSelectedMonth != null ->
                        s.copy(firstSelectedMonth = month, lastSelectedMonth = null)
                    else ->
                        s.copy(lastSelectedMonth = month)
                }
            }
            is AdvancedSearchAction.TuitionRangeChange -> _state.update { it.copy(tuitionRangeMax = action.maxValue) }
            is AdvancedSearchAction.DurationChange -> _state.update { it.copy(durationMaxYears = action.maxYears) }
            is AdvancedSearchAction.ScholarshipFilterChange -> _state.update { it.copy(scholarshipFilter = action.value) }
            AdvancedSearchAction.Submit -> submit()
        }
    }

    private fun submit() {
        viewModelScope.launch {
            val s = _state.value
            val filters = buildFilters(s)
            val ranges = buildRanges(s)
            val flags = buildFlags(s)
            val params = AdvancedSearchParams(
                filters = filters,
                ranges = ranges,
                flags = flags
            )
            _uiEffect.emit(AdvancedSearchUiEffect.NavigateToResults(searchText = "", advancedParams = params))
        }
    }

    private fun buildFilters(s: AdvancedSearchState): AdvancedFilters? {
        val countryIds = s.selectedCountry?.let { listOf(it.id) }
        val cityIds = s.selectedCity?.let { listOf(it.id) }
        val programmeIds = s.selectedCourse?.let { listOf(it.id) }
        val intake = if (s.selectedIntakeMonths.isNotEmpty()) {
            IntakeFilter(
                year = s.selectedIntakeYear,
                fromMonth = s.selectedIntakeMonths.min(),
                toMonth = s.selectedIntakeMonths.max()
            )
        } else null
        if (countryIds == null && cityIds == null && programmeIds == null && intake == null) return null
        return AdvancedFilters(
            countryIds = countryIds,
            cityIds = cityIds,
            programmeIds = programmeIds,
            intake = intake
        )
    }

    private fun buildRanges(s: AdvancedSearchState): AdvancedRanges? {
        val tuition = MinMax(min = 0, max = s.tuitionRangeMax)
        val durationMonths = MinMax(min = null, max = s.durationMaxYears * 12)
        return AdvancedRanges(tuitionFee = tuition, durationMonths = durationMonths)
    }

    private fun buildFlags(s: AdvancedSearchState): AdvancedFlags? {
        return s.scholarshipFilter?.let { AdvancedFlags(hasScholarship = it) }
    }
}
