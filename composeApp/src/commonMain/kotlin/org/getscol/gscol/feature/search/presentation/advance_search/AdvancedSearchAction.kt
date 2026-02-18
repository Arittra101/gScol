package org.getscol.gscol.feature.search.presentation.advance_search

import org.getscol.gscol.core.components.DropdownOption

sealed interface AdvancedSearchAction {
    data class CountrySelected(val option: DropdownOption?) : AdvancedSearchAction
    data class CitySelected(val option: DropdownOption?) : AdvancedSearchAction
    data class CourseSelected(val option: DropdownOption?) : AdvancedSearchAction
    data class IntakeYearSelected(val year: Int) : AdvancedSearchAction
    data class IntakeMonthToggled(val month: Int) : AdvancedSearchAction
    data class TuitionRangeChange(val maxValue: Int) : AdvancedSearchAction
    data class DurationChange(val maxYears: Int) : AdvancedSearchAction
    data class ScholarshipFilterChange(val value: Boolean?) : AdvancedSearchAction
    data object Submit : AdvancedSearchAction
}
