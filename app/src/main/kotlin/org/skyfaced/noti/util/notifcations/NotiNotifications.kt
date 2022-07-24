package org.skyfaced.noti.util.notifcations

import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

interface NotiNotifications {
    val snackbarHostState: SnackbarHostState

    suspend fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
    ): SnackbarResult

    fun hideSnackbar()

    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT)

    fun hideToast()
}