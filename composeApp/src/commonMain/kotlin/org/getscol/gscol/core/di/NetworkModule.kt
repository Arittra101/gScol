package org.getscol.gscol.core.di

//import org.getscol.gscol.core.data.network.MockHttpFactory
import com.getscol.gscol.BuildKonfig
import io.ktor.client.HttpClient
import org.getscol.gscol.core.data.network.HttpClientFactory
import org.koin.dsl.module

/**
 * Network module providing HTTP client and related dependencies
 */
val networkModule = module {
    single<HttpClient> {
        HttpClientFactory.createHttpClient(
            engine = get(),
            get(),
            BuildKonfig.BASE_URL
        )
    }
  //  single<HttpClient>(named("mock")) { MockHttpFactory.provideMockHttpClient() }
}


