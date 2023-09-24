package org.skyfaced.noti.util

sealed class ScreenState {
    data object Loading : ScreenState()

    data object Success : ScreenState()

    data object Empty : ScreenState()

    data object Failure : ScreenState()

    data object Unknown : ScreenState()
}