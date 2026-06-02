package org.getscol.gscol

import android.app.Application
import android.content.pm.ApplicationInfo
import org.getscol.gscol.core.data.storage.initializeAndroidContext
import org.getscol.gscol.core.di.initKoin
import org.getscol.gscol.core.utils.AppLogger
import org.koin.android.ext.koin.androidContext
import timber.log.Timber

class ScolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeAndroidContext(this)

        val isDebuggable = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        if (isDebuggable) {
            Timber.plant(Timber.DebugTree())
        } else {
            AppLogger.disableLogging()
        }

        initKoin {
            androidContext(this@ScolApplication)
        }
    }
}