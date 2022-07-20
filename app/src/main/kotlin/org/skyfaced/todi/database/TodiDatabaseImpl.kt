package org.skyfaced.todi.database

import android.content.Context
import androidx.room.Room
import logcat.logcat
import java.util.concurrent.Executors

class TodiDatabaseImpl(
    context: Context,
) {
    val database = Room.databaseBuilder(
        context,
        TodiDatabase::class.java, "todi.database"
    ).setQueryCallback({ query, args ->
        logcat(tag = "Database") { "\nQuery: $query" + if (args.isNotEmpty()) ";\nArgs: $args" else "" }
    }, Executors.newSingleThreadExecutor()).build()
}