package org.skyfaced.noti.util.exception

import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.Settings
import org.skyfaced.noti.settings.strings

class NoteNotInserted(
    locale: Settings<NotiLocale>
) : ResourceException(locale.strings.msg_note_not_inserted)