package org.skyfaced.noti.util

import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.Settings
import org.skyfaced.noti.util.exception.ResourceException
import org.skyfaced.noti.util.exception.UnexpectedException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()

    data class Failure<T>(val cause: ResourceException) : Result<T>()
}

interface ResultScope<T> {
    val locale: Settings<NotiLocale>

    fun success(data: T): Result<T>

    fun failure(cause: ResourceException): Result<T>
}

class ResultScopeImpl<T>(override val locale: Settings<NotiLocale>) : ResultScope<T> {
    override fun success(data: T): Result<T> {
        return Result.Success(data)
    }

    override fun failure(cause: ResourceException): Result<T> {
        return Result.Failure(cause)
    }
}

inline fun <T> result(
    locale: Settings<NotiLocale>,
    action: ResultScope<T>.() -> Result<T>
): Result<T> {
    return try {
        with(ResultScopeImpl(locale), action)
    } catch (e: Exception) {
        Result.Failure(UnexpectedException(locale))
    }
}

/** Idea stolen from [kotlin.Result] */
@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onSuccess(action: (data: T) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Success) action(data)
    return this
}

/** Idea stolen from [kotlin.Result] */
@OptIn(ExperimentalContracts::class)
inline fun <T> Result<T>.onFailure(action: (cause: ResourceException) -> Unit): Result<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    if (this is Result.Failure) action(cause)
    return this
}