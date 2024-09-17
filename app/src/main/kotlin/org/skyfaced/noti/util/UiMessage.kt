package org.skyfaced.noti.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import logcat.LogPriority
import logcat.logcat
import org.skyfaced.noti.util.exception.ResourceException
import java.util.UUID

/** @see <a href="https://github.com/chrisbanes/tivi/blob/main/common-ui-view/src/main/java/app/tivi/api/UiMessage.kt">Tivi sources</a> */
data class UiMessage(
    val message: String? = null,
    val cause: Throwable? = null,
    val id: Long = UUID.randomUUID().mostSignificantBits,
) {
    var data: Any? = null

    init {
        logcat(LogPriority.ERROR) {
            cause?.stackTraceToString() ?: cause?.message ?: "MessageRes: $message"
        }
    }
}

fun UiMessage(
    exception: ResourceException,
    id: Long = UUID.randomUUID().mostSignificantBits,
): UiMessage = UiMessage(
    message = exception.message.orEmpty(),
    cause = exception,
    id = id,
)

fun UiMessage.withData(data: Any): UiMessage {
    this.data = data
    return this
}

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())

    /**
     * A flow emitting the current message to display.
     */
    val message: Flow<UiMessage?> = _messages.map { it.firstOrNull() }.distinctUntilChanged()

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value = _messages.value + message
        }
    }

    suspend fun clearMessage(id: Long) {
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }
}
