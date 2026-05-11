package org.getscol.gscol.core.di

import android.app.Application
import android.content.SharedPreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.getscol.gscol.feature.profile.data.download.PublicDownloadsWriter
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

    single<HttpClientEngine> {
        OkHttp.create()
    }

    single { PublicDownloadsWriter(androidContext()) }
}
