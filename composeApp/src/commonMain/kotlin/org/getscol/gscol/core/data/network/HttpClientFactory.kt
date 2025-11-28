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
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.getscol.gscol.core.data.auth.AuthTokenProvider
import org.getscol.gscol.core.data.auth.AuthTokenResponse
import org.getscol.gscol.core.data.auth.RefreshTokenRequest
import org.getscol.gscol.navigation.LogoutEventManager
import org.getscol.gscol.navigation.NavigationAction

object HttpClientFactory {
    fun createHttpClient(
        engine: HttpClientEngine,
        tokenProvider: AuthTokenProvider,
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
                    override fun log(message: String) = println(message)
                }
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val accessToken = tokenProvider.getAccessToken().orEmpty()
                        val refreshToken = tokenProvider.getRefreshToken()
                        BearerTokens(accessToken = accessToken, refreshToken = refreshToken)
                    }
                    refreshTokens {
                        val oldRefreshToken = tokenProvider.getRefreshToken()
                        if (oldRefreshToken.isNullOrBlank()) {
                            tokenProvider.clearTokens()
                            LogoutEventManager.sendLogoutEvent(NavigationAction.NavigateToLogInScreen)
                            return@refreshTokens null
                        }

                        try {
                            val response: AuthTokenResponse = client.post("auth/refresh") {
                                contentType(ContentType.Application.Json)
                                setBody(RefreshTokenRequest(oldRefreshToken))
                                markAsRefreshTokenRequest()
                            }.body()

                            val newTokens = AuthTokenResponse(
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken,
                                expireTime = response.expireTime
                            )
                            tokenProvider.saveTokens(response.accessToken, response.refreshToken)

                            BearerTokens(
                                accessToken = newTokens.accessToken.orEmpty(),
                                refreshToken = newTokens.refreshToken
                            )

                        } catch (e: Exception) {
                            tokenProvider.clearTokens()
                            LogoutEventManager.sendLogoutEvent(NavigationAction.NavigateToLogInScreen)
                            null
                        }
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                headers {
                    if (!tokenProvider.getAccessToken().isNullOrBlank()) {
                        append("Authorization", "Bearer ${tokenProvider.getAccessToken()}")
                    }
                }
            }
        }
    }
}