package org.skyfaced.noti.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import logcat.logcat
import java.util.concurrent.Executors

class NotiDatabaseImpl(
    context: Context,
) {
    val database = Room.databaseBuilder(
        context,
        NotiDatabase::class.java,
        "noti.database"
    )
        .setQueryCallback(object : RoomDatabase.QueryCallback {
            override fun onQuery(query: String, args: List<Any?>) {
                logcat(tag = "Database") {
                    "\nQuery: $query" + if (args.isNotEmpty()) ";\nArgs: $args" else ""
                }
            }
        }, Executors.newSingleThreadExecutor())
        .build()
}