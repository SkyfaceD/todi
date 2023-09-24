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
            override fun onQuery(sqlQuery: String, bindArgs: List<Any?>) {
                logcat(tag = "Database") {
                    "\nQuery: $sqlQuery" + if (bindArgs.isNotEmpty()) ";\nArgs: $bindArgs" else ""
                }
            }
        }, Executors.newSingleThreadExecutor())
        .build()
}