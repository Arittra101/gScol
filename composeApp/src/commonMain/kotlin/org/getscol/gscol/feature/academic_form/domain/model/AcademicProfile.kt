package org.getscol.gscol.feature.academic_form.domain.model

data class AcademicProfile(
    val degrees: List<Degree>? = null,
    val englishTests: List<EnglishTest>? = null,
    val preferredPrograms: List<Preference>? = null,
    val preferredCountries: List<Preference>? = null
) {
    fun getLastDegree(): String {
        return degrees?.findLast { it.gpa != null && it.gpa != "" }?.name.orEmpty()
    }

    fun getSelectedEnglishTest(): EnglishTest? {
        return englishTests?.find { it.overallScore != null }
    }

    fun getSelectedCountryPref(): Preference? {
        return preferredCountries?.find { it.selected == true }
    }

    fun getSelectedProgrammePref(): Preference? {
        return preferredPrograms?.find { it.selected == true }
    }
}

data class Degree(
    val degreeId: String? = null,
    val name: String? = null,
    val gpa: String? = null
)

data class EnglishTest(
    val testId: String? = null,
    val testName: String? = null,
    val overallScore: Double? = null,
    val sections: List<TestSection>? = null
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
