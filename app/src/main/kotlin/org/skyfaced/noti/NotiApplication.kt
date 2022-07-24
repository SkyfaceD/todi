package org.skyfaced.noti

import android.app.Application
import logcat.AndroidLogcatLogger
import logcat.LogPriority

class NotiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidLogcatLogger.installOnDebuggableApp(this, minPriority = LogPriority.VERBOSE)
    }
}