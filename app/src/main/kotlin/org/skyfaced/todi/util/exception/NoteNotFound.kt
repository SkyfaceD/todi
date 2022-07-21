package org.skyfaced.todi.util.exception

import org.skyfaced.todi.R

class NoteNotFound(
    message: String? = null,
) : ResourceException(R.string.msg_note_not_found, message)