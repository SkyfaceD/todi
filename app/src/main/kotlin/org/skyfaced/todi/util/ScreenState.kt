package org.skyfaced.todi.util

sealed class ScreenState {
    object Loading : ScreenState()

    object Success : ScreenState()

    object Empty : ScreenState()

    object Failure : ScreenState()

    object Unknown : ScreenState()
}