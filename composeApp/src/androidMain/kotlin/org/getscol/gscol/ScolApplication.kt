package org.getscol.gscol

import android.app.Application
import org.getscol.gscol.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import timber.log.Timber

class ScolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        initKoin {
            androidContext(this@ScolApplication)
        }
    }
}