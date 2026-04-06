package org.getscol.gscol.feature.course_details.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import kotlinx.serialization.json.Json
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.course_details.data.dto.CourseDetailsResponseDto

class CourseDetailsApiServiceImpl(
    private val httpClient: HttpClient,
) : CourseDetailsApiService {

    override suspend fun getCourseDetails(courseId: String): Result<CourseDetailsResponseDto, DataError.Remote> {
        if (USE_DUMMY_COURSE_DETAILS_JSON) {
            return try {
                val json = dummyCourseDetailsResponseJson(courseId)
                Result.Success(courseDetailsJson.decodeFromString<CourseDetailsResponseDto>(json))
            } catch (_: Exception) {
                Result.Error(DataError.Remote.SERIALIZATION)
            }
        }
        return safeApiCall {
            httpClient.get("/course/details") {
                parameter("courseId", courseId)
                markAsNoAuth()
            }
        }
    }

    companion object {
        /**
         * Set to `false` when the real `/course/details` endpoint is ready.
         */
        private const val USE_DUMMY_COURSE_DETAILS_JSON = true

        private val courseDetailsJson = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
    }
}

/**
 * Sample payload aligned with the latest course-details contract.
 * [courseId] is interpolated so you can confirm the requested id in the UI.
 */
private fun dummyCourseDetailsResponseJson(courseId: String): String = """
{
  "status": "success",
  "message": "Operation completed successfully",
  "statusCode": 200,
  "data": {
    "courseDetails": {
      "courseId": "$courseId",
      "courseName": "Social Sciences — BA (international)",
      "ranking": {
        "position": 28,
        "hasInfo": true,
        "infoKey": "rankingMetaData"
      },
      "university": {
        "uniId": "2c5661f5-5a6c-453e-873d-f2d1a7ec510d",
        "uniName": "Cardiff University",
        "uniLogoUrl": "https://siuk-europe.s3.amazonaws.com/static/original_images/new-cardiff-logo.webp",
        "uniCoverImageUrl": "https://example.com/cover.png"
      },
      "tags": [
        { "label": "Estd. 1883", "type": "established" },
        { "label": "PUBLIC", "type": "type" },
        { "label": "Cardiff, Wales", "type": "location" }
      ],
      "tabs": [
        { "key": "aboutUs", "label": "About Us" },
        { "key": "campusLife", "label": "Campus Life" },
        { "key": "location", "label": "Location" },
        { "key": "academicRequirements", "label": "Academic Info" },
        { "key": "feesAndScholarships", "label": "Fees & Scholarships" },
        { "key": "intakeDates", "label": "Intake Dates" }
      ],
      "aboutUs": {
        "description": [
          "Cardiff University is recognised in independent government assessments as one of Britain's leading teaching and research universities. The university combines impressive modern facilities and an exciting approach to teaching with its proud history of recent achievements, and over 32,000 students, including nearly 6,400 international students, are currently enrolled at the Welsh institution.\\nCardiff University's research and research-led teaching expertise involves social sciences, engineering, technology and a wide range of business professions."
        ]
      },
      "campusLife": {
        "media": {
          "videoUrl": [
            "https://www.youtube.com/watch?v=SOxqUyBCo8E",
            "https://www.youtube.com/watch?v=r7R8ewU1Wqc"
          ]
        }
      },
      "location": {
        "city": "Cardiff",
        "country": "Wales",
        "state": "Wales",
        "address": "Cardiff, Wales",
        "coordinates": {
          "link": "https://maps.app.goo.gl/vx8Lbwd5o79xroqf7%20https://gostudyin.com/wp-content/uploads/2025/12/new-cardiff-logo.webp"
        }
      },
      "academicRequirements": {
        "hasInfo": true,
        "infoKey": "academicRequirementsMetaData",
        "requirements": {
          "degreeRequirements": [
            { "degreeName": "HSC", "label": "GPA | CGPA", "minValue": "5.00" }
          ],
          "englishRequirements": [
            { "testName": "IELTS", "minOverallValue": "6.50", "minSectionValue": "6.00" },
            { "testName": "TOEFL", "minOverallValue": "90.00", "minSectionValue": "20.00" },
            { "testName": "PTE", "minOverallValue": "60.00", "minSectionValue": "59.00" }
          ]
        }
      },
      "feesAndScholarships": {
        "hasInfo": true,
        "infoKey": "feesAndScholarshipsMetaData",
        "items": {
          "tuitionFees": {
            "amount": "17000",
            "currency": "GBP",
            "frequency": "yearly"
          },
          "initialDeposit": "3000",
          "applicationFee": "0",
          "scholarships": {
            "scholarshipName": "International Merit Scholarship",
            "scholarshipAmount": "1200",
            "currency": "GBP",
            "scholarshipType": "PERCENTAGE"
          }
        }
      },
      "intakeDates": {
        "hasInfo": true,
        "infoKey": "intakeDatesMetaData",
        "intakes": [
          "January",
          "April",
          "September"
        ]
      }
    },
    "meta": [
      {
        "infoKey": "rankingMetaData",
        "title": "Ranking",
        "information": [
          {
            "subtitle": "Ranking",
            "description": [
              "Cardiff University holds a prominent place in UK higher education, ranking in the top 30 universities in the UK in the Times University Guide 2026.",
              "28th in the Times University Guide 2026",
              "37th in the Guardian University Guide 2026",
              "181st in QS World University Rankings 2026"
            ]
          }
        ]
      },
      {
        "infoKey": "academicRequirementsMetaData",
        "title": "Academic Requirements",
        "information": [
          {
            "subtitle": "Entry requirements",
            "description": [
              "Applicants should hold HSC or equivalent; minimum GPA 5.00 where applicable.",
              "Programme-specific prerequisites may apply; confirm on the official course page before applying."
            ]
          },
          {
            "subtitle": "English requirement",
            "description": [
              "IELTS Academic: 6.5 overall with no sub-test below 6.0, or equivalent TOEFL/PTE scores."
            ]
          }
        ]
      },
      {
        "infoKey": "feesAndScholarshipsMetaData",
        "title": "Fees & Scholarships",
        "information": [
          {
            "subtitle": "feesMetaData",
            "description": [
              "Indicative tuition and fees for planning only — verify with the university.",
              "Tuition year: 2026"
            ]
          },
          {
            "subtitle": "ScholarshipsMetaData",
            "description": [
              "Scholarships: Merit-based and need-based awards may be available. Check the scholarships office or apply during admissions."
            ]
          }
        ]
      },
      {
        "infoKey": "intakeDatesMetaData",
        "title": "Intake Dates",
        "information": [
          {
            "description": [
              "January",
              "April",
              "September"
            ]
          }
        ]
      }
    ]
  }
}
""".trimIndent()
