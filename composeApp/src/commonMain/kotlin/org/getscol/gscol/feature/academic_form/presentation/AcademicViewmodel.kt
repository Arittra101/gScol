package org.getscol.gscol.feature.academic_form.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.core.helper.orFalse
import org.getscol.gscol.feature.academic_form.data.mapper.toAcademicInfoRequest
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile
import org.getscol.gscol.feature.academic_form.domain.model.EnglishTest
import org.getscol.gscol.feature.academic_form.domain.model.Preference
import org.getscol.gscol.feature.academic_form.domain.repository.AcademicRepository

@OptIn(ExperimentalCoroutinesApi::class)
class AcademicViewmodel(
    private val academicRepository: AcademicRepository,
    private val session: Session
) : ViewModel() {

    private val fetchAcademicInfo = MutableStateFlow(Unit)
    private val localUpdates = MutableStateFlow<AcademicUiState?>(null)

    val academicUiState: StateFlow<AcademicUiState> = fetchAcademicInfo
        .flatMapLatest {
            academicRepository.fetchAcademicInfo()
                .map { result ->
                    when (result) {
                        is Result.Success -> initDataBindOnAcademicUiState(result.data)
                        is Result.Error -> AcademicUiState(
                            showLoader = false,
                            isApiSuccess = false,
                            successMsg = "Something went wrong. Please try Later",
                            showApiResponseBottomSheet = true
                        )
                    }
                }
        }.combine(localUpdates) { remoteState, localState ->
            localState ?: remoteState
        }.stateIn(
            scope = viewModelScope,
            SharingStarted.Lazily,
            initialValue = AcademicUiState(showLoader = true)
        )

    fun onAction(action: AcademicFormAction) {
        when (action) {
            is AcademicFormAction.OnSscChange -> updateSscGpa(action.gpa)
            is AcademicFormAction.OnBscChange -> updateBscGpa(action.gpa)
            is AcademicFormAction.OnHscChange -> updateHscGpa(action.gpa)
            is AcademicFormAction.OnMscChange -> updateMscGpa(action.gpa)
            is AcademicFormAction.OnLastInstituteChange -> updateLastInstitute(action.instituteName)
            is AcademicFormAction.OnTestTypeChange -> selectTestType(action.testId)
            is AcademicFormAction.OnTestScoreChange -> updateTestScoreAndTestList(
                action.testSectionId,
                action.testScore
            )

            is AcademicFormAction.OnOverallScoreChange -> updateOverallScoreAndTestList(
                action.overallScore,
                action.isDuolingo
            )

            is AcademicFormAction.OnCountryPrefChange -> selectCountryPref(
                action.countryName,
                action.countryId
            )

            is AcademicFormAction.OnProgrammePrefChange -> selectProgrammePref(
                action.programmeName,
                action.programmeId
            )

            is AcademicFormAction.SubmitAcademicForm -> submitAcademicInfoForm()
            is AcademicFormAction.OnUnselectTestType -> unSelectTestType()
            is AcademicFormAction.DismissApiResponseSheet -> localUpdates.value =
                academicUiState.value.copy(showApiResponseBottomSheet = false)

        }
    }

    fun fetchAcademicInfo() {
        fetchAcademicInfo.value = Unit
    }

    private fun initDataBindOnAcademicUiState(data: AcademicProfile): AcademicUiState {
        val unselectedEnglishTest = EnglishTest(testName = "Unselect English Test")
        val updatedTestList = listOf(unselectedEnglishTest) + (data.englishTests ?: emptyList())

        return AcademicUiState(
            ssc = data.degrees?.getOrNull(0),
            hsc = data.degrees?.getOrNull(1),
            bsc = data.degrees?.getOrNull(2),
            msc = data.degrees?.getOrNull(3),
            lastInstitute = data.lastInstituteName,
            lastInstituteEditable = data.isLastInstituteEditable(),
            selectedTestType = data.getSelectedEnglishTest(),
            selectedProgrammePreference = data.getSelectedProgrammePref(),
            selectedCountryPreference = data.getSelectedCountryPref(),
            selectedCountryPreferenceEditable = data.isPreferredCountriesEditable(),
            selectedProgrammePreferenceEditable = data.isPreferredProgrammeEditable(),
            showLoader = false,
            testTypeList = updatedTestList,
            programmePreferenceList = data.preferredPrograms,
            programmeCountryList = data.preferredCountries,
        )
    }

    private fun unSelectTestType() {
        val current = academicUiState.value
        localUpdates.value = current.copy(selectedTestType = current.testTypeList?.getOrNull(0))
        buttonState(true)
    }

    private fun submitAcademicInfoForm() {
        showLoader(true)
        viewModelScope.launch {
            val result = academicRepository.updateAcademicInfo(academicUiState.value.toAcademicInfoRequest())
            when (result) {
                is Result.Success -> {
                    val current = academicUiState.value
                    session.triggerAcademicFormSubmission()
                    localUpdates.value = current.copy(
                        showLoader = false,
                        isApiSuccess = true,
                        successMsg = "Your academic info was submitted successfully.",
                        showApiResponseBottomSheet = true
                    )
                }

                is Result.Error -> {
                    val current = academicUiState.value
                    localUpdates.value = current.copy(
                        showLoader = false,
                        isApiSuccess = false,
                        successMsg = "Something went wrong. Please try Later",
                        showApiResponseBottomSheet = true
                    )
                }
            }
        }
    }

    private fun showLoader(isShow : Boolean){
        val current = academicUiState.value
        localUpdates.value = current.copy(showLoader = isShow)
    }

    private fun updateSscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            ssc = current.ssc?.copy(gpa = gpa)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun updateHscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            hsc = current.hsc?.copy(gpa = gpa)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun updateBscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            bsc = current.bsc?.copy(gpa = gpa)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun updateMscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            msc = current.msc?.copy(gpa = gpa)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun updateLastInstitute(instituteName: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            lastInstitute = instituteName
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun selectCountryPref(countryName: String, countryId: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            selectedCountryPreference = current.selectedCountryPreference?.copy(
                name = countryName,
                id = countryId
            ) ?: Preference(id = countryId, name = countryName)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun selectProgrammePref(programmeName: String, programmeId: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            selectedProgrammePreference = current.selectedProgrammePreference?.copy(
                name = programmeName,
                id = programmeId
            ) ?: Preference(id = programmeId, name = programmeName)
        )
        val isEnable =
            (current.selectedTestType?.readyForSubmit == true) || (current.selectedTestType?.testId == null)
        buttonState(isEnable)
    }

    private fun selectTestType(testId: String) {
        val current = academicUiState.value
        val selectedTestType = current.testTypeList?.find { it.testId == testId }
        localUpdates.value = current.copy(
            selectedTestType = selectedTestType
        )
        println("at first ${selectedTestType?.readyForSubmit}")

        val isEnable = selectedTestType?.readyForSubmit.orFalse()
        buttonState(isEnable)
    }

    private fun updateOverallScoreAndTestList(overallScore: String, isDuolingo: Boolean? = null) {

        val current = academicUiState.value
        val selectedTestType = current.selectedTestType ?: return


        if (isDuolingo != null) {
            val readyForSubmit = overallScore.isNotEmpty()
            val updatedSelectedTestType =
                selectedTestType.copy(readyForSubmit = readyForSubmit, overallScore = overallScore)
            val updatedTestTypeList = current.testTypeList?.map { test ->
                if (test.testId == updatedSelectedTestType.testId) {
                    updatedSelectedTestType
                } else {
                    test
                }
            }
            localUpdates.value = current.copy(
                selectedTestType = updatedSelectedTestType,
                testTypeList = updatedTestTypeList
            )
            buttonState(readyForSubmit)
            return
        }


        val sectionsScoreList = selectedTestType.sections?.map { it.score?.toFloatOrNull() }

        val isUserFillup = sectionsScoreList?.all { it != null }.orFalse() && (overallScore.isNotEmpty())
        val updatedSelectedTestType = selectedTestType.copy(overallScore = overallScore, readyForSubmit = isUserFillup)

        val updatedTestTypeList = current.testTypeList?.map { test ->
            if (test.testId == updatedSelectedTestType.testId) {
                updatedSelectedTestType
            } else {
                test
            }
        }

        localUpdates.value = current.copy(
            selectedTestType = updatedSelectedTestType,
            testTypeList = updatedTestTypeList
        )
        buttonState(updatedSelectedTestType.readyForSubmit.orFalse())
    }

    //method
    private fun updateTestScoreAndTestList(testSectionId: String, testScore: String) {
        val current = academicUiState.value
        val selectedTestType = current.selectedTestType ?: return

        //update score in selectedType
        val updatedSelectedTestTypeSections = selectedTestType.sections?.map { section ->
            if (section.id == testSectionId) {
                section.copy(score = testScore)
            } else {
                section
            }
        }

        val readyForSubmit = updatedSelectedTestTypeSections?.let { sections ->
            val parsedScores = sections.map { it.score?.toFloatOrNull() }
            parsedScores.all { it != null } && !selectedTestType.overallScore.isNullOrEmpty()
        } ?: false


        val updatedSelectedTestType = selectedTestType.copy(
            sections = updatedSelectedTestTypeSections,
            readyForSubmit = readyForSubmit
        )

        //repace updatedSelectedTestType by old in the list
        val updatedTestTypeList = current.testTypeList?.map { test ->
            if (test.testId == selectedTestType.testId) {
                updatedSelectedTestType
            } else {
                test
            }
        }

        localUpdates.value = current.copy(
            selectedTestType = updatedSelectedTestType,
            testTypeList = updatedTestTypeList
        )

        buttonState(updatedSelectedTestType.readyForSubmit.orFalse())
    }

    private fun buttonState(enable: Boolean) {
        val current = academicUiState.value

        if (isInitialState()) {
            localUpdates.value = current.copy(enableSubmitButton = false)
            return
        }

        localUpdates.value = current.copy(enableSubmitButton = enable)
    }

    private fun isInitialState(): Boolean {
        val current = academicUiState.value

        val academicInitialSate = isAcademicInfoInitialState()

        //last institute
        val isLastInstituteInitialState = (current.lastInstituteEditable == false) || (current.lastInstituteEditable == true && current.lastInstitute.isNullOrEmpty())

        // selected type initial state
        val isSelectedTypeInitialState = (current.selectedTestType?.testId == null) || (current.selectedTestType.editable == false)

        // selected pref country
        val isSelectedProgrammePrefInitialState = (current.selectedProgrammePreferenceEditable == false) || (current.selectedProgrammePreferenceEditable == true && current.selectedProgrammePreference == null)

        val isSelectedCountryPrefInitialState = (current.selectedCountryPreferenceEditable == false) || (current.selectedCountryPreferenceEditable == true && current.selectedCountryPreference == null)


        return academicInitialSate
                && isLastInstituteInitialState
                && isSelectedTypeInitialState
                && isSelectedProgrammePrefInitialState
                && isSelectedCountryPrefInitialState

    }

    private fun isAcademicInfoInitialState(): Boolean {
        val current = academicUiState.value

        return listOfNotNull(
            current.ssc,
            current.hsc,
            current.bsc,
            current.msc
        ).filter { it.editable }.all { it.gpa.isNullOrEmpty() }
    }

}