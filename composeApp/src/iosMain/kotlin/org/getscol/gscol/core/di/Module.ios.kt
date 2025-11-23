package org.getscol.gscol.core.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.getscol.gscol.core.data.auth.AuthTokenProvider
import org.getscol.gscol.core.data.auth.IosAuthTokenProvider
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

/**
 * IOS-specific module support

 */

actual val platformModule: Module = module {

    single<NSUserDefaults> { NSUserDefaults.Companion.standardUserDefaults() }

    single<AuthTokenProvider> { IosAuthTokenProvider(get()) }

    single<HttpClientEngine> {
        Darwin.create()
    }
}
