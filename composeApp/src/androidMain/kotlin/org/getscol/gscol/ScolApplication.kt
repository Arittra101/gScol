package org.getscol.gscol

import android.app.Application
import org.example.scol_chuker.CmpChucker
import org.example.scol_chuker.initializeContext
import org.getscol.gscol.core.data.storage.initializeAndroidContext
import org.getscol.gscol.core.di.initKoin
import org.koin.android.ext.koin.androidContext
import timber.log.Timber

class ScolApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeAndroidContext(this)

        Timber.plant(Timber.DebugTree())

        initializeContext(this)
        val chuckerModule = CmpChucker.getChuckerModules()
        initKoin(chuckerModule) {
            androidContext(this@ScolApplication)
        }
    }
}