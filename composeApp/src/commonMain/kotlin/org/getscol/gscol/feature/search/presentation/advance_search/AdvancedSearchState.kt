package org.getscol.gscol.feature.search.presentation.advance_search

import org.getscol.gscol.core.components.DropdownOption

data class AdvancedSearchState(
    val countryOptions: List<DropdownOption> = emptyList(),
    val cityOptions: List<DropdownOption> = emptyList(),
    val courseOptions: List<DropdownOption> = emptyList(),
    val selectedCountry: DropdownOption? = null,
    val selectedCity: DropdownOption? = null,
    val selectedCourse: DropdownOption? = null,
    val selectedIntakeYear: Int = 2026,
    val selectedIntakeMonths: Set<Int> = emptySet(),
    val tuitionRangeMax: Int = 50_000,
    val durationMaxYears: Int = 5,
    val scholarshipFilter: Boolean? = null
)
