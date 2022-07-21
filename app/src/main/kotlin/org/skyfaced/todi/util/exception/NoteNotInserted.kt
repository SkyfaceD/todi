package org.skyfaced.todi.util.exception

import org.skyfaced.todi.R

class NoteNotInserted(
    message: String? = null,
) : ResourceException(R.string.msg_note_not_inserted, message)