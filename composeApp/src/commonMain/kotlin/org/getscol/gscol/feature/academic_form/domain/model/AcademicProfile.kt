package org.getscol.gscol.feature.academic_form.domain.model

data class AcademicProfile(
    val degrees: List<Degree>? = null,
    val englishTests: List<EnglishTest>? = null,
    val preferredPrograms: List<Preference>? = null,
    val preferredCountries: List<Preference>? = null,
    val lastInstituteName: String? = null
) {
    fun getSelectedEnglishTest(): EnglishTest? {
        return englishTests?.find { it.overallScore !=  "" }
    }

    fun getSelectedCountryPref(): Preference? {
        return preferredCountries?.find { it.selected == true }
    }

    fun getSelectedProgrammePref(): Preference? {
        return preferredPrograms?.find { it.selected == true }
    }

    fun isLastInstituteEditable(): Boolean? {
        return lastInstituteName == null
    }

    fun isPreferredCountriesEditable(): Boolean? {
        return getSelectedCountryPref() == null
    }

    fun isPreferredProgrammeEditable(): Boolean? {
        return getSelectedProgrammePref() == null
    }

}

data class Degree(
    val degreeId: String? = null,
    val name: String? = null,
    val gpa: String? = null,
    val editable: Boolean = true,
)

data class EnglishTest(
    val testId: String? = null,
    val testName: String? = null,
    val overallScore: String? = null,
    val sections: List<TestSection>? = null,
    val maxScore: Int? = null,
    val editable: Boolean? = true,
    val readyForSubmit: Boolean? = null,
)

data class TestSection(
    val id: String? = null,
    val name: String? = null,
    val score: String? = null
)

data class Preference(
    val id: String? = null,
    val name: String? = null,
    val selected: Boolean? = null
)
