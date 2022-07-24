package org.skyfaced.noti.util.exception

import org.skyfaced.noti.R

class NoteNotInserted(
    message: String? = null,
) : ResourceException(R.string.msg_note_not_inserted, message)