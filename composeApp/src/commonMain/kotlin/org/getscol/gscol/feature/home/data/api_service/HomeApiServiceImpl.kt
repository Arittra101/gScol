package org.getscol.gscol.feature.home.data.api_service

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.contentType
import org.getscol.gscol.core.data.network.markAsNoAuth
import org.getscol.gscol.core.data.network.safeApiCall
import org.getscol.gscol.core.domain.DataError
import org.getscol.gscol.core.domain.Result
import org.getscol.gscol.feature.home.data.dto.HomeResponseDto

class HomeApiServiceImpl(private val httpClient: HttpClient) : HomeApiService {
    override suspend fun getHomeData(page: Int, limit: Int, isLogin: Boolean): Result<HomeResponseDto, DataError.Remote> {
        return safeApiCall {
            httpClient.get("api/home/courses") {
                contentType(ContentType.Application.Json)
                parameter("page", page)
                parameter("limit", limit)
                parameter("isLogin", isLogin)
                if (!isLogin) markAsNoAuth()
            }
        }
    }
}



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
}