package org.skyfaced.todi

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority

class TodiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}