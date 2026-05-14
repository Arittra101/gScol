package org.getscol.gscol.core.utils

import android.app.Activity
import android.content.Context

private var appContext: Context? = null

fun setContext(context: Context) {
    appContext = context
}

actual fun closeApp() {
    (appContext as? Activity)?.finish()
}

