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
    val firstSelectedMonth: Int? = null,
    val lastSelectedMonth: Int? = null,
    val tuitionRangeMax: Int = 50_000,
    val durationMaxYears: Int = 5,
    val scholarshipFilter: Boolean? = null
) {
    /** Selected months as a range: first click = one month, second click = from first to last (inclusive). */
    val selectedIntakeMonths: Set<Int>
        get() = when {
            firstSelectedMonth == null -> emptySet()
            lastSelectedMonth == null -> setOf(firstSelectedMonth)
            else -> (minOf(firstSelectedMonth, lastSelectedMonth)..maxOf(firstSelectedMonth, lastSelectedMonth)).toSet()
        }
}
