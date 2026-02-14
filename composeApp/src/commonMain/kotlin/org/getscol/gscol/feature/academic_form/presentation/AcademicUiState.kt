package org.getscol.gscol.feature.academic_form.presentation

import org.getscol.gscol.feature.academic_form.domain.model.Degree
import org.getscol.gscol.feature.academic_form.domain.model.EnglishTest
import org.getscol.gscol.feature.academic_form.domain.model.Preference

data class AcademicUiState(
    val ssc: Degree? = null,
    val hsc: Degree? = null,
    val bsc: Degree? = null,
    val msc: Degree? = null,
    val lastInstitute: String? = null,
    val selectedTestType: EnglishTest? = null,
    val selectedProgrammePreference: Preference? = null,
    val selectedCountryPreference: Preference? = null,
    val testTypeList: List<EnglishTest>? = null,
    val programmePreferenceList: List<Preference>? = null,
    val programmeCountryList: List<Preference>? = null,
    val showLoader: Boolean = true,
)

fun List<Preference>.toListDropDownUiModel() = map { it.toDropDownUiModel() }

fun List<EnglishTest>.toListDropDownUiModel2() = map { it.toDropDownUiModel2() }

fun Preference.toDropDownUiModel() = DropDownUiModel(id.orEmpty(), name.orEmpty())

fun EnglishTest.toDropDownUiModel2() = DropDownUiModel(testId.orEmpty(), testName.orEmpty())


data class DropDownUiModel(
    val id: String,
    val itemName: String
)