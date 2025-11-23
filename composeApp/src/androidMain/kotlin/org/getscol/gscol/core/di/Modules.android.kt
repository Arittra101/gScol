package org.getscol.gscol.core.di

import android.app.Application
import android.content.SharedPreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.getscol.gscol.core.data.auth.AndroidAuthTokenProvider
import org.getscol.gscol.core.data.auth.AuthTokenProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific module support
 * Provides Android platform implementations
 */

actual val platformModule: Module = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences("auth_prefs", Application.MODE_PRIVATE)
    }

    single<AuthTokenProvider> { AndroidAuthTokenProvider(get()) }

    single<HttpClientEngine> {
        OkHttp.create()
    }

}

