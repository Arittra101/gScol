package org.getscol.gscol.feature.academic_form.data.mapper

import org.getscol.gscol.core.helper.orFalse
import org.getscol.gscol.core.helper.toStringOrEmpty
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoDtoResponse
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoRequest
import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicResultRequest
import org.getscol.gscol.feature.academic_form.data.academic_dto.DegreeDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.EnglishSectionRequest
import org.getscol.gscol.feature.academic_form.data.academic_dto.EnglishTestDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.EnglishTestResultRequest
import org.getscol.gscol.feature.academic_form.data.academic_dto.PreferenceDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.TestSectionDto
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile
import org.getscol.gscol.feature.academic_form.domain.model.Degree
import org.getscol.gscol.feature.academic_form.domain.model.EnglishTest
import org.getscol.gscol.feature.academic_form.domain.model.Preference
import org.getscol.gscol.feature.academic_form.domain.model.TestSection
import org.getscol.gscol.feature.academic_form.presentation.AcademicUiState


fun AcademicInfoDtoResponse.toAcademicProfile(): AcademicProfile {
    return AcademicProfile(
        degrees = data?.degrees.orEmpty().toDegrees(),
        englishTests = data?.englishTests.orEmpty().toEnglishTests(),
        preferredPrograms = data?.preferredPrograms?.toPreference(),
        preferredCountries = data?.preferredCountries?.toPreference(),
        lastInstituteName = data?.lastAcademicInstitute
    )
}

fun List<PreferenceDto>.toPreference(): List<Preference> = map { it.toPreference() }


fun PreferenceDto.toPreference(): Preference = Preference(
    id = id,
    name = name,
    selected = selected
)


fun List<DegreeDto>.toDegrees(): List<Degree> {
    return map { it.toDegree() }
}


fun DegreeDto.toDegree(): Degree {
    return Degree(
        degreeId = degreeId.orEmpty(),
        name = name.orEmpty(),
        gpa = gpa?.toString() ?: "",
        editable = isEditable.orFalse()
    )
}

fun List<EnglishTestDto>.toEnglishTests(): List<EnglishTest> {
    return map { it.toEnglishTest() }
}

fun EnglishTestDto.toEnglishTest(): EnglishTest {
    val resolvedEditable = isEditable ?: true // for initial case  editable = false fillup true
    return EnglishTest(
        testId = testId.orEmpty(),
        testName = testName.orEmpty(),
        overallScore = overallScore.toStringOrEmpty(),
        sections = sections.orEmpty().toTestSections(),
        maxScore = validation?.maxScore,
        editable = resolvedEditable,
        readyForSubmit = !resolvedEditable
    )
}

fun List<TestSectionDto>.toTestSections(): List<TestSection> {
    return map {
        TestSection(
            id = it.id.orEmpty(),
            name = it.name.orEmpty(),
            score = it.score.toStringOrEmpty()
        )
    }
}


fun AcademicUiState.toAcademicInfoRequest(): AcademicInfoRequest {

    val academicResults = listOfNotNull(ssc, hsc, bsc, msc)
        .filter { degree ->
            degree.editable && !degree.gpa.isNullOrEmpty()
        }
        .mapNotNull { degree ->
            degree.gpa?.toDoubleOrNull()?.let { gpaValue ->
                AcademicResultRequest(
                    degreeId = degree.degreeId,
                    gpa = gpaValue
                )
            }
        }.takeIf { it.isNotEmpty() }


    val lastInstituteName = if (lastInstituteEditable == true && !lastInstitute.isNullOrEmpty()) lastInstitute else null

    val englishTestResults = testTypeList
        .orEmpty()
        .mapNotNull { test ->
            val overall = test.overallScore?.toDoubleOrNull()
            if (test.editable == true && overall != null) {
                EnglishTestResultRequest(
                    testId = test.testId,
                    overallScore = overall,
                    sections = test.sections?.mapNotNull { section ->
                        section.score?.toDoubleOrNull()?.let { scoreValue ->
                            EnglishSectionRequest(
                                id = section.id,
                                score = scoreValue
                            )
                        }
                    }
                )
            } else null
        }.takeIf { it.isNotEmpty() }

    return AcademicInfoRequest(
        academicResults = academicResults,
        englishTestResults = englishTestResults,
        lastAcademicInstitute = lastInstituteName,
        preferredCountryIds = listOfNotNull(selectedCountryPreference?.id).takeIf { selectedCountryPreferenceEditable == true },
        preferredProgrammeIds = listOfNotNull(selectedProgrammePreference?.id).takeIf { selectedProgrammePreferenceEditable == true }
    )
}
