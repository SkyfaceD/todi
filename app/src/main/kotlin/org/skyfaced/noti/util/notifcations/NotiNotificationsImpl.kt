package org.skyfaced.noti.util.notifcations

import android.content.Context
import android.widget.Toast
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

class NotiNotificationsImpl(
    private val context: Context,
    override val snackbarHostState: SnackbarHostState,
) : NotiNotifications {
    private var toast: Toast? = null

    override suspend fun showSnackbar(
        message: String,
        actionLabel: String?,
        withDismissAction: Boolean,
        duration: SnackbarDuration,
    ): SnackbarResult {
        return snackbarHostState.showSnackbar(message, actionLabel, withDismissAction, duration)
    }

    override fun hideSnackbar() {
        snackbarHostState.currentSnackbarData?.dismiss()
    }

    override fun showToast(message: String, duration: Int) {
        toast = Toast.makeText(context, message, duration)
        toast?.show()
    }

    override fun hideToast() {
        toast?.cancel()
        toast = null
    }
}