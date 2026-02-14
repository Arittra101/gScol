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
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.getscol.gscol.core.data.session.Session
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile
import org.getscol.gscol.feature.academic_form.domain.model.Preference
import org.getscol.gscol.feature.academic_form.domain.repository.AcademicRepository

@OptIn(ExperimentalCoroutinesApi::class)
class AcademicViewmodel(
    private val academicRepository: AcademicRepository,
    private val session: Session
) : ViewModel() {

    private var academicProfile: AcademicProfile? = null
    private val fetchAcademicInfo = MutableStateFlow(Unit)
    private val localUpdates = MutableStateFlow<AcademicUiState?>(null)

    val academicUiState: StateFlow<AcademicUiState> = fetchAcademicInfo
        .flatMapLatest {
            academicRepository.fetchAcademicInfo()
                .onEach { result ->
                    if (result is Result.Success) academicProfile = result.data
                }
                .map { result ->
                    println("print flatmap")
                    when (result) {
                        is Result.Success -> {
                            initDataBindOnAcademicUiState(result.data)
                        }

                        is Result.Error -> {
                            AcademicUiState()
                        }
                    }
                }
        }.combine(localUpdates) { remoteState, localState ->
            localState ?: remoteState
        }.stateIn(
            scope = viewModelScope,
            SharingStarted.Lazily,
            initialValue = AcademicUiState(showLoader = true)
        )

    private fun initDataBindOnAcademicUiState(data: AcademicProfile): AcademicUiState {
        return AcademicUiState(
            ssc = data.degrees?.getOrNull(0),
            hsc = data.degrees?.getOrNull(1),
            bsc = data.degrees?.getOrNull(2),
            msc = data.degrees?.getOrNull(3),
            lastInstitute = data.getLastDegree(),
            selectedTestType = data.getSelectedEnglishTest(),
            selectedProgrammePreference = data.getSelectedProgrammePref(),
            selectedCountryPreference = data.getSelectedCountryPref(),
            testTypeList = data.englishTests,
            programmePreferenceList = data.preferredPrograms,
            programmeCountryList = data.preferredCountries,
            showLoader = false
        )
    }

    fun onAction(action: AcademicFormAction){
        when(action) {
            is AcademicFormAction.OnSscChange -> {
                updateSscGpa(action.gpa)
            }
            is AcademicFormAction.OnBscChange -> {
                updateBscGpa(action.gpa)
            }
            is AcademicFormAction.OnHscChange -> {
                updateHscGpa(action.gpa)
            }
            is AcademicFormAction.OnMscChange -> {
                updateMscGpa(action.gpa)
            }

            is AcademicFormAction.OnTestTypeChange -> {
                selectTestType(action.testId)
            }

            is AcademicFormAction.OnCountryPrefChange -> {
                selectCountryPref(action.countryName,action.countryId)
            }
            is AcademicFormAction.OnProgrammePrefChange -> {
                selectProgrammePref(action.programmeName,action.programmeId)
            }

            is AcademicFormAction.OnTestScoreChange -> {
                updateTestScoreAndTestList(action.testSectionId,action.testScore)
            }
        }
    }

    fun fetchAcademicInfo() {
        fetchAcademicInfo.value = Unit
    }

    private fun updateSscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            ssc = current.ssc?.copy(gpa = gpa)
        )
    }

    private fun updateHscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            hsc = current.hsc?.copy(gpa = gpa)
        )
    }

    private fun updateBscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            bsc = current.bsc?.copy(gpa = gpa)
        )
    }

    private fun updateMscGpa(gpa: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            msc = current.msc?.copy(gpa = gpa)
        )
    }

    private fun selectCountryPref(countryName: String, countryId: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            selectedCountryPreference = current.selectedCountryPreference?.copy(
                name = countryName,
                id = countryId
            ) ?: Preference(id = countryId, name = countryName)
        )
    }

    private fun selectProgrammePref(programmeName: String, programmeId: String) {
        val current = academicUiState.value
        localUpdates.value = current.copy(
            selectedProgrammePreference = current.selectedProgrammePreference?.copy(
                name = programmeName,
                id = programmeId
            ) ?: Preference(id = programmeId, name = programmeName)
        )
    }


    private fun selectTestType(testId: String){
        val current = academicUiState.value
        val selectedTestType = current.testTypeList?.find { it.testId == testId }
        localUpdates.value = current.copy(
            selectedTestType = selectedTestType
        )
    }

    private fun updateTestScoreAndTestList(testSectionId: String, testScore: String) {
        val current = academicUiState.value
        val selectedTest = current.selectedTestType ?: return

        // Update the specific section's score in the selected test
        val updatedSections = selectedTest.sections?.map { section ->
            if (section.id == testSectionId) {
                section.copy(score = testScore)
            } else {
                section
            }
        }

        val updatedTest = selectedTest.copy(sections = updatedSections)

        // Update BOTH selectedTestType AND testTypeList
        val updatedTestTypeList = current.testTypeList?.map { test ->
            if (test.testId == selectedTest.testId) {
                updatedTest  // Replace with updated version
            } else {
                test  // Keep others unchanged
            }
        }

        localUpdates.value = current.copy(
            selectedTestType = updatedTest,
            testTypeList = updatedTestTypeList  // Update the source list too
        )
    }
}