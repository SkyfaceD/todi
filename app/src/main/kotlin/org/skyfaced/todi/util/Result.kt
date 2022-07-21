package org.skyfaced.todi.util

import org.skyfaced.todi.util.exception.ResourceException
import org.skyfaced.todi.util.exception.UnexpectedException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure<T>(val cause: ResourceException) : Result<T>()
}

suspend fun <T> result(action: suspend () -> Result<T>): Result<T> {
    return try {
        action()
    } catch (e: Exception) {
        Result.Failure(UnexpectedException())
    }
}

fun <T> Result<T>.consume(
    onSuccess: (data: T) -> Unit = {},
    onFailure: (ResourceException) -> Unit = {},
) {
    when (this) {
        is Result.Success -> onSuccess(this.data)
        is Result.Failure -> onFailure(this.cause)
    }
}