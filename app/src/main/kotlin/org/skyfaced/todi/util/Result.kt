package org.skyfaced.todi.util

import org.skyfaced.todi.util.exception.ResourceException
import org.skyfaced.todi.util.exception.UnexpectedException

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure<T>(val cause: ResourceException) : Result<T>()
}

interface ResultScope<T> {
    fun success(data: T): Result<T>

    fun failure(cause: ResourceException): Result<T>
}

class ResultScopeImpl<T> : ResultScope<T> {
    override fun success(data: T): Result<T> {
        return Result.Success(data)
    }

    override fun failure(cause: ResourceException): Result<T> {
        return Result.Failure(cause)
    }
}

suspend fun <T> result(
    scope: ResultScope<T> = ResultScopeImpl(),
    action: suspend ResultScope<T>.() -> Result<T>,
): Result<T> {
    return try {
        action(scope)
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