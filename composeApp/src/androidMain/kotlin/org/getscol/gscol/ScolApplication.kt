package org.getscol.gscol

import android.app.Application
import org.getscol.gscol.core.di.initKoin
import org.koin.android.ext.koin.androidContext

class ScolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ScolApplication)
        }
    }
}