package org.getscol.gscol.core.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific module support
 * Provides Android platform implementations
 */

actual val platformModule: Module = module {
    single<HttpClientEngine> {
        OkHttp.create()
    }
}

