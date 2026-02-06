//package org.getscol.gscol.core.data.network
//
//import io.ktor.client.HttpClient
//import io.ktor.client.engine.mock.MockEngine
//import io.ktor.client.engine.mock.respond
//import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
//import io.ktor.http.ContentType
//import io.ktor.http.HttpHeaders
//import io.ktor.http.HttpStatusCode
//import io.ktor.http.content.TextContent
//import io.ktor.http.headersOf
//import io.ktor.serialization.kotlinx.json.json
//import kotlinx.serialization.json.Json
//import org.getscol.gscol.feature.home.data.api_service.homeJson
//import org.getscol.gscol.feature.home.data.api_service.homeJsonLoggedIn
//import org.getscol.gscol.feature.home.domain.model.CourseRequest
//
//object MockHttpFactory {
//    fun provideMockHttpClient(): HttpClient =
//        HttpClient(MockEngine) {
//            install(ContentNegotiation) {
//                json(Json {
//                    ignoreUnknownKeys = true
//                    isLenient = true
//                    prettyPrint = false
//                })
//            }
//
//            engine {
//                addHandler { request ->
//                    //val page = request.url.parameters["page"]?.toInt() ?: 1
//                    val isLogin = request.url.parameters["isLogin"]?.toBoolean() ?: false
//
//                    val cursor = try {
//                        val bodyText = when (val body = request.body) {
//                            is TextContent -> body.text
//                            else -> ""
//                        }
//                        if (bodyText.isNotEmpty()) {
//                            Json.decodeFromString<CourseRequest>(bodyText).pagination.cursor
//                        } else {
//                            null
//                        }
//                    } catch (e: Exception) {
//                        println("error=> ${e.message}")
//                        null
//                    }
//
//
//                    val json = if (isLogin) {
//                        println("Call homeJsonLoggedIn")
//                        homeJsonLoggedIn(cursor)
//                    } else {
//                        println("Call homeJson")
//                        homeJson(cursor)
//                    }
//
//                    respond(
//                        content = json,
//                        status = HttpStatusCode.OK,
//                        headers = headersOf(
//                            HttpHeaders.ContentType,
//                            ContentType.Application.Json.toString()
//                        )
//                    )
//                }
//            }
//        }
//}