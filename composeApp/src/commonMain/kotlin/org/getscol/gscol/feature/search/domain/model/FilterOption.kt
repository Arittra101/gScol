package org.getscol.gscol.feature.search.domain.model

data class FilterOption(
    val id: String,
    val name: String
)

data class AdvancedFilterOptions(
    val countryOptions: List<FilterOption>,
    val programmeOptions: List<FilterOption>
)
