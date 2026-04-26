package org.getscol.gscol.feature.profile.data.api_service

import kotlinx.serialization.json.Json
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.profile.data.dto.EditProfileDtoResponse

class DummyEditProfileApiServiceImpl(
    private val json: Json = Json { ignoreUnknownKeys = true },
) : EditProfileApiService {

    override suspend fun fetchEditProfile(): Result<EditProfileDtoResponse, DataError.Remote> {
        return try {
            val dto = json.decodeFromString(EditProfileDtoResponse.serializer(), DUMMY_EDIT_PROFILE_JSON)
            Result.Success(dto)
        } catch (_: Exception) {
            Result.Error(DataError.Remote.SERIALIZATION)
        }
    }
}

private const val DUMMY_EDIT_PROFILE_JSON: String =
    """
{
  "student": {
    "personalInformation": {
      "sectionTitle": "Personal Information",
      "joined": "2023",
      "img_url": "",
      "isEditable": true,
      "fields": [
        { "id": "full_name", "label": "Full Name", "value": "Sophia Carter" },
        { "id": "date_of_birth", "label": "Date of Birth", "value": "2002-05-15" },
        { "id": "gender", "label": "Gender", "value": "Female" }
      ]
    },
    "academicBackground": {
      "sectionTitle": "Academic Background",
      "isEditable": true,
      "fields": [
        { "id": "high_school", "label": "High School", "value": "Northwood High School" },
        { "id": "ssc_gpa", "label": "SSC GPA", "value": 5.00 },
        { "id": "college", "label": "College", "value": "Notre Dame College" },
        { "id": "hsc_gpa", "label": "HSC GPA", "value": 4.83 },
        { "id": "bachelors_cgpa", "label": "Bachelor's CGPA", "value": null },
        { "id": "masters_cgpa", "label": "Master's CGPA", "value": null },
        { "id": "standardized_tests", "label": "Standardized Tests", "value": "SAT: 1450" }
      ]
    },
    "englishTestScores": {
      "sectionTitle": "English Test Scores",
      "isEditable": false,
      "fields": [
        { "id": "ielts", "label": "IELTS", "value": "7.0" },
        { "id": "pte", "label": "PTE", "value": 76 },
        { "id": "moi", "label": "MOI", "value": "yes" }
      ]
    },
    "contactInformation": {
      "sectionTitle": "Contact Information",
      "isEditable": true,
      "fields": [
        { "id": "email", "label": "Email", "value": "sophia.c@email.com" },
        { "id": "phone", "label": "Phone", "value": "(555) 123-4567" }
      ]
    },
    "academicRecords": {
      "sectionTitle": "Academic Records",
      "items": [
        {
          "id": "doc_001",
          "label": "High School Transcript",
          "type": "HIGH_SCHOOL_TRANSCRIPT",
          "download_url": "https://storage.example.com/transcripts/sophia_hs.pdf",
          "status": { "code": "UPLOADED", "value": "In progress" }
        },
        {
          "id": "doc_002",
          "label": "College Transcript",
          "type": "COLLEGE_TRANSCRIPT",
          "download_url": "https://storage.example.com/transcripts/sophia_college.pdf",
          "status": { "code": "UNDER_REVIEW", "value": "Rejected" }
        },
        {
          "id": "doc_003",
          "label": "Standardized Test Scores",
          "type": "STANDARDIZED_TEST_SCORES",
          "download_url": "https://storage.example.com/tests/sophia_sat.pdf",
          "status": { "code": "VERIFIED", "value": "Verified" }
        }
      ]
    }
  }
}
"""

