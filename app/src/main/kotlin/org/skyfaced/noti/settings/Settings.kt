package org.skyfaced.noti.settings

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import logcat.logcat

interface Settings<T> {
    suspend fun update(newState: T) {
        logcat { "Update: ${newState.toString()}" }
    }

    val observe: Flow<T>

    suspend operator fun invoke(newState: T) = update(newState = newState)
}

suspend fun <T> Settings<T>.blockingFirst(): T {
    return withContext(Dispatchers.IO) {
        return@withContext runBlocking {
            observe.first()
        }
    }
}