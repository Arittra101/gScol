package org.getscol.gscol.feature.academic_form.presentation

sealed interface AcademicFormAction {
    data class OnSscChange(val gpa: String) : AcademicFormAction
    data class OnHscChange(val gpa: String) : AcademicFormAction
    data class OnBscChange(val gpa: String) : AcademicFormAction
    data class OnMscChange(val gpa: String) : AcademicFormAction

    data class OnLastInstituteChange(val instituteName: String) : AcademicFormAction

    data class OnTestTypeChange(val testId: String) : AcademicFormAction
    data class OnTestScoreChange(val testSectionId: String, val testScore: String): AcademicFormAction
    data class OnOverallScoreChange(val overallScore: String, val isDuolingo: Boolean?=null): AcademicFormAction

    data class OnCountryPrefChange(val countryName: String, val countryId: String): AcademicFormAction
    data class OnProgrammePrefChange(val programmeName: String, val programmeId: String): AcademicFormAction

    data object SubmitAcademicForm: AcademicFormAction
    data object OnUnselectTestType: AcademicFormAction
    data object DismissApiResponseSheet: AcademicFormAction
}
