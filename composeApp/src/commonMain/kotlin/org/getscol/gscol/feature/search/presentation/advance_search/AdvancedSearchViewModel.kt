package org.getscol.gscol.feature.search.presentation.advance_search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.getscol.gscol.feature.search.domain.model.AdvancedFilters
import org.getscol.gscol.feature.search.domain.model.AdvancedFlags
import org.getscol.gscol.feature.search.domain.model.AdvancedRanges
import org.getscol.gscol.feature.search.domain.model.AdvancedSearchParams
import org.getscol.gscol.feature.search.domain.model.MinMax

sealed interface AdvancedSearchUiEffect {
    data class NavigateToResults(val searchText: String, val advancedParams: AdvancedSearchParams) : AdvancedSearchUiEffect
}

class AdvancedSearchViewModel : ViewModel() {

    private val _state = MutableStateFlow(AdvancedSearchState())
    val state = _state.asStateFlow()

    private val _uiEffect = MutableSharedFlow<AdvancedSearchUiEffect>(replay = 0)
    val uiEffect = _uiEffect.asSharedFlow()

    fun onAction(action: AdvancedSearchAction) {
        when (action) {
            is AdvancedSearchAction.CountrySelected -> _state.update { it.copy(selectedCountry = action.option) }
            is AdvancedSearchAction.CitySelected -> _state.update { it.copy(selectedCity = action.option) }
            is AdvancedSearchAction.CourseSelected -> _state.update { it.copy(selectedCourse = action.option) }
            is AdvancedSearchAction.IntakeSelected -> _state.update { it.copy(selectedIntake = action.option) }
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
        val intakeIds = s.selectedIntake?.let { listOf(it.id) }
        if (countryIds == null && cityIds == null && programmeIds == null && intakeIds == null) return null
        return AdvancedFilters(
            countryIds = countryIds,
            cityIds = cityIds,
            programmeIds = programmeIds,
            intakeIds = intakeIds,
            intakeYear = null
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
