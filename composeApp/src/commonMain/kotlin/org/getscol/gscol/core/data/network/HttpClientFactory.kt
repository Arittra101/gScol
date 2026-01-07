package org.getscol.gscol.core.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.getscol.gscol.auth.data.AuthTokenProvider
import org.getscol.gscol.core.data.dto.auth.AuthTokenResponse
import org.getscol.gscol.core.data.dto.auth.RefreshTokenRequest
import org.getscol.gscol.core.utils.AppLogger
import org.getscol.gscol.navigation.LogoutEventManager
import org.getscol.gscol.navigation.NavigationAction

object HttpClientFactory {
    fun createHttpClient(
        engine: HttpClientEngine,
        tokenProvider: AuthTokenProvider,
        baseUrl: String
    ): HttpClient {

        return HttpClient(engine) {

            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 20_000L
                requestTimeoutMillis = 20_000L
            }

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) = AppLogger.d(message)
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken().orEmpty()
                        val refreshToken = tokenProvider.getRefreshToken()
                        if (accessToken.isBlank()) {
                            AppLogger.d("No access token available")
                            return@loadTokens null
                        }
                        AppLogger.d("access token ${accessToken}")
                        AppLogger.d("refreshToken  ${refreshToken}")
                        BearerTokens(accessToken = accessToken,  refreshToken = refreshToken.orEmpty())
                    }
                    refreshTokens {
                        AppLogger.d("go for refreshTokens")
                        val oldRefreshToken = tokenProvider.getRefreshToken()
                        AppLogger.d("go for refreshTokens")
                        if (oldRefreshToken.isNullOrBlank()) {
                            tokenProvider.clearTokens()
                            LogoutEventManager.sendLogoutEvent(NavigationAction.NavigateToLogInScreen)
                            return@refreshTokens null
                        }

                        try {
                            // Request new tokens
                            val response: AuthTokenResponse = client.post("auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(oldRefreshToken))
                                markAsNoAuth()
                            }.body()

                            // Save new tokens
                            tokenProvider.saveTokens(
                                response.accessToken,
                                response.refreshToken
                            )

                            // Return new tokens to Auth plugin
                            BearerTokens(
                                accessToken = response.accessToken.orEmpty(),
                                refreshToken = response.refreshToken.orEmpty()
                            )

                        } catch (e: Exception) {
                            AppLogger.e("Refresh token failed", e)
                            tokenProvider.clearTokens()
                            LogoutEventManager.sendLogoutEvent(
                                NavigationAction.NavigateToLogInScreen
                            )
                            null
                        }
                    }
                    sendWithoutRequest { request ->
                        // Don't send bearer token for requests marked as no-auth
                        !request.isMarkedAsNoAuth()
                    }
                }
            }

            defaultRequest {
                url(baseUrl)
                contentType(ContentType.Application.Json)
            }
        }
    }
}