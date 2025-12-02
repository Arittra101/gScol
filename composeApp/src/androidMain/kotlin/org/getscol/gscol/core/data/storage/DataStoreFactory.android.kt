package org.getscol.gscol.core.data.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

private lateinit var applicationContext: Context

fun initializeAndroidContext(context: Context) {
    applicationContext = context.applicationContext
}

actual fun createPlatformDataStore(): DataStore<Preferences> {
    return createDataStore {
        applicationContext.filesDir
            .resolve(DATA_STORE_FILE_NAME)
            .absolutePath
    }
}