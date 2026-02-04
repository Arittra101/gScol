package org.getscol.gscol.feature.home.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.data.dto.HomeResponseDto
import org.getscol.gscol.feature.home.domain.model.CourseRequest

class HomeApiServiceImpl(private val httpClient: HttpClient) : HomeApiService {
    override suspend fun getHomeData(courseRequest: CourseRequest, isLogin: Boolean): Result<HomeResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.post("/home") {
                contentType(ContentType.Application.Json)
               /* parameter("isLogin", isLogin)*/
                setBody(courseRequest)
                if (!isLogin) markAsNoAuth()
            }
        }
    }
}


fun homeJson(cursor: String? = null): String {
    // Generate different cursors based on current cursor
    val currentPage = when (cursor) {
        null -> 1
        "eyJyYW5rU2NvcmUiOjk1MDAuLi4" -> 2
        "eyJyYW5rU2NvcmUiOjg1MDAuLi4" -> 3
        else -> 1
    }

    return when (currentPage) {
        1 -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "NOT_LOGGED_IN",
            "academicFormStatus": "INCOMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": "eyJyYW5rU2NvcmUiOjk1MDAuLi4",
              "limit": 15,
              "hasNext": true
            },
            "courses": [
              {
                "courseId": "0e7ae5ee-ab0f-40a8-ae87-a340d07e43d6",
                "courseName": "Cyber Security (Advanced)",
                "university": {
                  "id": "83cad6a9-84a2-4d0e-a130-b57f7212b527",
                  "name": "Sydney International University",
                  "country": "Australia",
                  "state": "New South Wales",
                  "city": "Sydney",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "November 2026",
                  "year": 2026
                },
                "tuitionFee": 22000,
                "currency": "AUD",
                "durationMonths": 24,
                "initialDeposit": null,
                "applicationFee": null,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 90, "section": 20.0 }
                ],
                "isWishlisted": false
              },
              {
                "courseId": "1f8be6ff-bc1g-51b9-bf98-b451e18f54e7",
                "courseName": "Computer Science",
                "university": {
                  "id": "94dbe7ba-95b3-5e1f-b241-c68g8323c638",
                  "name": "University of London",
                  "country": "United Kingdom",
                  "state": "England",
                  "city": "London",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "September 2025",
                  "year": 2025
                },
                "tuitionFee": 18000,
                "currency": "GBP",
                "durationMonths": 36,
                "initialDeposit": 2000,
                "applicationFee": 100,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 88, "section": 19.0 }
                ],
                "isWishlisted": false
              },
              {
                "courseId": "2g9cf7gg-cd2h-62c0-cg09-c562f29g65f8",
                "courseName": "Data Science & Analytics",
                "university": {
                  "id": "a5ecf8cb-a6c4-6f2g-c352-d79h9434d749",
                  "name": "University of Toronto",
                  "country": "Canada",
                  "state": "Ontario",
                  "city": "Toronto",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "January 2026",
                  "year": 2026
                },
                "tuitionFee": 25000,
                "currency": "CAD",
                "durationMonths": 24,
                "initialDeposit": 3000,
                "applicationFee": 150,
                "isScholarshipAvailable": false,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 7.0, "section": 6.5 },
                  { "testName": "TOEFL", "overall": 95, "section": 22.0 }
                ],
                "isWishlisted": false
              }
            ]
          }
        }
        """.trimIndent()

        2 -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "NOT_LOGGED_IN",
            "academicFormStatus": "INCOMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": "eyJyYW5rU2NvcmUiOjg1MDAuLi4",
              "limit": 15,
              "hasNext": true
            },
            "courses": [
              {
                "courseId": "3h0dg8hh-de3i-73d1-dh10-d673g30h76g9",
                "courseName": "Artificial Intelligence",
                "university": {
                  "id": "b6fdf9dc-b7d5-7g3h-d463-e80i0545e850",
                  "name": "TU Berlin",
                  "country": "Germany",
                  "state": "Berlin",
                  "city": "Berlin",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "October 2025",
                  "year": 2025
                },
                "tuitionFee": 0,
                "currency": "EUR",
                "durationMonths": 24,
                "initialDeposit": null,
                "applicationFee": 75,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.0, "section": 5.5 },
                  { "testName": "TOEFL", "overall": 80, "section": 18.0 }
                ],
                "isWishlisted": false
              },
              {
                "courseId": "4i1eh9ii-ef4j-84e2-ei21-e784h41i87h0",
                "courseName": "Software Engineering",
                "university": {
                  "id": "c7geg0ed-c8e6-8h4i-e574-f91j1656f961",
                  "name": "University of Manchester",
                  "country": "United Kingdom",
                  "state": "England",
                  "city": "Manchester",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "September 2025",
                  "year": 2025
                },
                "tuitionFee": 19500,
                "currency": "GBP",
                "durationMonths": 36,
                "initialDeposit": 2500,
                "applicationFee": 120,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 90, "section": 20.0 }
                ],
                "isWishlisted": false
              }
            ]
          }
        }
        """.trimIndent()

        else -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "NOT_LOGGED_IN",
            "academicFormStatus": "INCOMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": null,
              "limit": 15,
              "hasNext": false
            },
            "courses": [
              {
                "courseId": "5j2fi0jj-fg5k-95f3-fj32-f895i52j98i1",
                "courseName": "Robotics Engineering",
                "university": {
                  "id": "d8hfh1fe-d9f7-9i5j-f685-g02k2767g072",
                  "name": "University of Tokyo",
                  "country": "Japan",
                  "state": "Tokyo",
                  "city": "Tokyo",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "April 2026",
                  "year": 2026
                },
                "tuitionFee": 15000,
                "currency": "JPY",
                "durationMonths": 24,
                "initialDeposit": 1500,
                "applicationFee": 100,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 88, "section": 19.0 }
                ],
                "isWishlisted": false
              }
            ]
          }
        }
        """.trimIndent()
    }
}

fun homeJsonLoggedIn(cursor: String? = null): String {
    val currentPage = when (cursor) {
        null -> 1
        "eyJyYW5rU2NvcmUiOjg3MDAuLi4" -> 2
        "eyJyYW5rU2NvcmUiOjc3MDAuLi4" -> 3
        else -> 1
    }

    return when (currentPage) {
        1 -> """
        {
          "status": "success",
          "message": "Eligible courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "LOGGED_IN",
            "academicFormStatus": "COMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": "eyJyYW5rU2NvcmUiOjg3MDAuLi4",
              "limit": 15,
              "hasNext": true
            },
            "courses": [
              {
                "courseId": "5j2fi0jj-fg5k-95f3-fj32-f895i52j98i1",
                "courseName": "Master of Computer Science",
                "university": {
                  "id": "d8hfh1fe-d9f7-9i5j-f685-g02k2767g072",
                  "name": "Imperial College London",
                  "country": "United Kingdom",
                  "state": "England",
                  "city": "London",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "September 2025",
                  "year": 2025
                },
                "tuitionFee": 28000,
                "currency": "GBP",
                "durationMonths": 24,
                "initialDeposit": 3500,
                "applicationFee": 150,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 7.0, "section": 6.5 },
                  { "testName": "TOEFL", "overall": 100, "section": 22.0 }
                ],
                "isWishlisted": true
              },
              {
                "courseId": "6k3gj1kk-gh6l-06g4-gk43-g906j63k09j2",
                "courseName": "Machine Learning & AI",
                "university": {
                  "id": "e9igi2gf-e0g8-0j6k-g796-h13l3878h183",
                  "name": "University of Melbourne",
                  "country": "Australia",
                  "state": "Victoria",
                  "city": "Melbourne",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "February 2026",
                  "year": 2026
                },
                "tuitionFee": 35000,
                "currency": "AUD",
                "durationMonths": 24,
                "initialDeposit": 4000,
                "applicationFee": 100,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 90, "section": 20.0 }
                ],
                "isWishlisted": false
              },
              {
                "courseId": "7l4hk2ll-hi7m-17h5-hl54-h017k74l10k3",
                "courseName": "Information Technology Management",
                "university": {
                  "id": "f0jhj3hg-f1h9-1k7l-h807-i24m4989i294",
                  "name": "University of British Columbia",
                  "country": "Canada",
                  "state": "British Columbia",
                  "city": "Vancouver",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "September 2025",
                  "year": 2025
                },
                "tuitionFee": 32000,
                "currency": "CAD",
                "durationMonths": 16,
                "initialDeposit": 3500,
                "applicationFee": 125,
                "isScholarshipAvailable": false,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 90, "section": 21.0 }
                ],
                "isWishlisted": true
              }
            ]
          }
        }
        """.trimIndent()

        2 -> """
        {
          "status": "success",
          "message": "Eligible courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "LOGGED_IN",
            "academicFormStatus": "COMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": "eyJyYW5rU2NvcmUiOjc3MDAuLi4",
              "limit": 15,
              "hasNext": true
            },
            "courses": [
              {
                "courseId": "8m5il3mm-ij8n-28i6-im65-i128l85m21l4",
                "courseName": "Cloud Computing & DevOps",
                "university": {
                  "id": "g1kik4ih-g2i0-2l8m-i918-j35n5090j305",
                  "name": "National University of Singapore",
                  "country": "Singapore",
                  "state": null,
                  "city": "Singapore",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "August 2025",
                  "year": 2025
                },
                "tuitionFee": 38000,
                "currency": "SGD",
                "durationMonths": 18,
                "initialDeposit": 5000,
                "applicationFee": 200,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 6.5, "section": 6.0 },
                  { "testName": "TOEFL", "overall": 92, "section": 20.0 }
                ],
                "isWishlisted": false
              },
              {
                "courseId": "9n6jm4nn-jk9o-39j7-jn76-j239m96n32m5",
                "courseName": "Blockchain Technology",
                "university": {
                  "id": "h2ljl5ji-h3j1-3m9n-j029-k46o6101k416",
                  "name": "ETH Zurich",
                  "country": "Switzerland",
                  "state": "Zurich",
                  "city": "Zurich",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "September 2025",
                  "year": 2025
                },
                "tuitionFee": 1500,
                "currency": "CHF",
                "durationMonths": 24,
                "initialDeposit": null,
                "applicationFee": 150,
                "isScholarshipAvailable": false,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 7.0, "section": 6.5 },
                  { "testName": "TOEFL", "overall": 100, "section": 22.0 }
                ],
                "isWishlisted": true
              }
            ]
          }
        }
        """.trimIndent()

        else -> """
        {
          "status": "success",
          "message": "Eligible courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "LOGGED_IN",
            "academicFormStatus": "COMPLETE",
            "listType": "ELIGIBLE_ONLY",
            "pagination": {
              "cursor": null,
              "limit": 15,
              "hasNext": false
            },
            "courses": [
              {
                "courseId": "0a7ko5oo-kl0p-40k8-ko87-k340o07o43o6",
                "courseName": "Quantum Computing",
                "university": {
                  "id": "i3nmk6kj-i4k2-4o0o-k140-l57p7212l527",
                  "name": "MIT",
                  "country": "United States",
                  "state": "Massachusetts",
                  "city": "Cambridge",
                  "logoUrl": null,
                  "imgUrl": null
                },
                "imgUrl": null,
                "intake": {
                  "name": "Fall 2025",
                  "year": 2025
                },
                "tuitionFee": 55000,
                "currency": "USD",
                "durationMonths": 24,
                "initialDeposit": 5000,
                "applicationFee": 250,
                "isScholarshipAvailable": true,
                "engRequirements": [
                  { "testName": "IELTS", "overall": 7.5, "section": 7.0 },
                  { "testName": "TOEFL", "overall": 110, "section": 25.0 }
                ],
                "isWishlisted": false
              }
            ]
          }
        }
        """.trimIndent()
    }
}

/*
fun homeJson(page: Int): String {
    return when (page) {
        1 -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "GUEST",
            "listType": "ALL",
            "pagination": {
              "page": 1,
              "limit": 8,
              "totalItems": 24,
              "totalPages": 3,
              "hasNext": true
            },
            "all_courses": [
              { "universityId": "U1", "courseId": "C1", "city": "London", "courseName": "Computer Science", "universityName": "University of London", "country": "UK", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 12000, "currency": "GBP", "duration": "3 Years", "scholarship": 2000, "deposit": 1000, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U2", "courseId": "C2", "city": "Manchester", "courseName": "Software Engineering", "universityName": "Manchester University", "country": "UK", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 13000, "currency": "GBP", "duration": "3 Years", "scholarship": 1500, "deposit": 1200, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U3", "courseId": "C3", "city": "Toronto", "courseName": "Data Science", "universityName": "University of Toronto", "country": "Canada", "imageUrl": null, "intake": "Winter 2026", "tuitionFee": 14000, "currency": "CAD", "duration": "2 Years", "scholarship": 3000, "deposit": 1500, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.5", "isWishlisted": false },
              { "universityId": "U4", "courseId": "C4", "city": "Sydney", "courseName": "Information Technology", "universityName": "University of Sydney", "country": "Australia", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 16000, "currency": "AUD", "duration": "3 Years", "scholarship": 2500, "deposit": 1800, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U5", "courseId": "C5", "city": "Berlin", "courseName": "Artificial Intelligence", "universityName": "TU Berlin", "country": "Germany", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 9000, "currency": "EUR", "duration": "2 Years", "scholarship": 1000, "deposit": 800, "ieltsBandRequired": "6.0", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U6", "courseId": "C6", "city": "Paris", "courseName": "Cyber Security", "universityName": "Sorbonne University", "country": "France", "imageUrl": null, "intake": "Spring 2026", "tuitionFee": 11000, "currency": "EUR", "duration": "2 Years", "scholarship": 1200, "deposit": 900, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U7", "courseId": "C7", "city": "New York", "courseName": "Computer Engineering", "universityName": "NYU", "country": "USA", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 20000, "currency": "USD", "duration": "4 Years", "scholarship": 4000, "deposit": 2500, "ieltsBandRequired": "7.0", "ieltsOverallRequired": "6.5", "isWishlisted": false },
              { "universityId": "U8", "courseId": "C8", "city": "Tokyo", "courseName": "Robotics", "universityName": "University of Tokyo", "country": "Japan", "imageUrl": null, "intake": "Spring 2026", "tuitionFee": 15000, "currency": "JPY", "duration": "2 Years", "scholarship": 1800, "deposit": 1300, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false }
            ]
          }
        }
        """.trimIndent()

        2 -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "GUEST",
            "listType": "ALL",
            "pagination": {
              "page": 2,
              "limit": 8,
              "totalItems": 24,
              "totalPages": 3,
              "hasNext": true
            },
            "all_courses": [
              { "universityId": "U9", "courseId": "C9", "city": "Rome", "courseName": "Information Systems", "universityName": "Sapienza University", "country": "Italy", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 10000, "currency": "EUR", "duration": "3 Years", "scholarship": 1200, "deposit": 900, "ieltsBandRequired": "6.0", "ieltsOverallRequired": "6.0", "isWishlisted": false },
              { "universityId": "U10", "courseId": "C10", "city": "Madrid", "courseName": "Computer Science", "universityName": "Complutense University", "country": "Spain", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 9500, "currency": "EUR", "duration": "3 Years", "scholarship": 1100, "deposit": 850, "ieltsBandRequired": "6.0", "ieltsOverallRequired": "6.0", "isWishlisted": false }
            ]
          }
        }
        """.trimIndent()

        else -> """
        {
          "status": "success",
          "message": "Courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "GUEST",
            "listType": "ALL",
            "pagination": {
              "page": 3,
              "limit": 8,
              "totalItems": 24,
              "totalPages": 3,
              "hasNext": false
            },
            "all_courses": [
              { "universityId": "U17", "courseId": "C17", "city": "Seoul", "courseName": "AI Engineering", "universityName": "Seoul National University", "country": "South Korea", "imageUrl": null, "intake": "Spring 2026", "tuitionFee": 14000, "currency": "KRW", "duration": "2 Years", "scholarship": 2000, "deposit": 1200, "ieltsBandRequired": "6.5", "ieltsOverallRequired": "6.0", "isWishlisted": false }
            ]
          }
        }
        """.trimIndent()
    }
}

fun homeJsonLoggedIn(page: Int): String {
    return when (page) {
        1 -> """
        {
          "status": "success",
          "message": "Eligible courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "LOGGED_IN",
            "listType": "ELIGIBLE",
            "pagination": {
              "page": 1,
              "limit": 8,
              "totalItems": 16,
              "totalPages": 2,
              "hasNext": true
            },
            "eligible": {
              "courses": [
                { "universityId": "EU1", "courseId": "EC1", "city": "London", "courseName": "AI & ML", "universityName": "Imperial College", "country": "UK", "imageUrl": null, "intake": "Fall 2025", "tuitionFee": 18000, "currency": "GBP", "duration": "2 Years", "scholarship": 3000, "deposit": 2000, "ieltsBandRequired": "7.0", "ieltsOverallRequired": "6.5", "isWishlisted": true }
              ]
            }
          }
        }
        """.trimIndent()

        else -> """
        {
          "status": "success",
          "message": "Eligible courses retrieved successfully",
          "statusCode": 200,
          "data": {
            "userState": "LOGGED_IN",
            "listType": "ELIGIBLE",
            "pagination": {
              "page": 2,
              "limit": 8,
              "totalItems": 16,
              "totalPages": 2,
              "hasNext": false
            },
            "eligible": {
              "courses": []
            }
          }
        }
        """.trimIndent()
    }
}*/