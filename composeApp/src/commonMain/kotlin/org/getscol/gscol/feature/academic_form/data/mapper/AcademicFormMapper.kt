package org.getscol.gscol.feature.academic_form.data.mapper

import org.getscol.gscol.feature.academic_form.data.academic_dto.AcademicInfoDtoResponse
import org.getscol.gscol.feature.academic_form.data.academic_dto.DegreeDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.EnglishTestDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.PreferenceDto
import org.getscol.gscol.feature.academic_form.data.academic_dto.TestSectionDto
import org.getscol.gscol.feature.academic_form.domain.model.AcademicProfile
import org.getscol.gscol.feature.academic_form.domain.model.Degree
import org.getscol.gscol.feature.academic_form.domain.model.EnglishTest
import org.getscol.gscol.feature.academic_form.domain.model.Preference
import org.getscol.gscol.feature.academic_form.domain.model.TestSection


fun AcademicInfoDtoResponse.toAcademicProfile(): AcademicProfile {
    return AcademicProfile(
        degrees = data?.degrees.orEmpty().toDegrees(),
        englishTests = data?.englishTests.orEmpty().toEnglishTests(),
        preferredPrograms = data?.preferredPrograms?.toPreference(),
        preferredCountries = data?.preferredCountries?.toPreference()
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
        gpa = gpa?.toString() ?: ""
    )
}

fun List<EnglishTestDto>.toEnglishTests(): List<EnglishTest> {
    return map { it.toEnglishTest() }
}

fun EnglishTestDto.toEnglishTest(): EnglishTest {
    return EnglishTest(
        testId = testId.orEmpty(),
        testName = testName.orEmpty(),
        overallScore = overallScore,
        sections = sections.orEmpty().toTestSections()
    )
}

fun List<TestSectionDto>.toTestSections(): List<TestSection> {
    return map {
        TestSection(
            id = it.id.orEmpty(),
            name = it.name.orEmpty(),
            score = it.score?.toString() ?: ""
        )
    }
}

