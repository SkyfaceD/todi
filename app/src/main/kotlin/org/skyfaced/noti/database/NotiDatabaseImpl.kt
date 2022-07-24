package org.skyfaced.noti.database

import android.content.Context
import androidx.room.Room
import logcat.logcat
import java.util.concurrent.Executors

class NotiDatabaseImpl(
    context: Context,
) {
    val database = Room.databaseBuilder(
        context,
        NotiDatabase::class.java, "noti.database"
    ).setQueryCallback({ query, args ->
        logcat(tag = "Database") { "\nQuery: $query" + if (args.isNotEmpty()) ";\nArgs: $args" else "" }
    }, Executors.newSingleThreadExecutor()).build()
}