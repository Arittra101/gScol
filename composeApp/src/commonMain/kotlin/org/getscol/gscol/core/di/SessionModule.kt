package org.getscol.gscol.core.di

import org.getscol.gscol.core.data.session.AppSession
import org.getscol.gscol.core.data.session.Session
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sessionModule = module {
    singleOf(::AppSession).bind<Session>()
}