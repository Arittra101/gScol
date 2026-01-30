package org.getscol.gscol.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.getscol.gscol.feature.home.data.api_service.homeJson
import org.getscol.gscol.feature.home.data.api_service.homeJsonLoggedIn

object MockHttpFactory {
    fun provideMockHttpClient(): HttpClient =
        HttpClient(MockEngine) {
            // Add ContentNegotiation plugin for proper JSON deserialization
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = false
                })
            }

            engine {
                addHandler { request ->
                    val page = request.url.parameters["page"]?.toInt() ?: 1
//                    val hasAuthHeader = request.headers["Authorization"]?.startsWith("Bearer") == true
                    val hasAuthHeader = request.url.parameters["isLogin"]?.toBoolean() ?: false

                    val json = if (hasAuthHeader) {
                        println("Call homeJsonLoggedIn")
                        homeJsonLoggedIn(page)
                    } else {
                        println("Call homeJson")  // FIXED: was printing "homeJsonLoggedIn"
                        homeJson(page)
                    }

                    respond(
                        content = json,
                        status = HttpStatusCode.OK,
                        headers = headersOf(
                            HttpHeaders.ContentType,
                            ContentType.Application.Json.toString()
                        )
                    )
                }
            }
        }
}