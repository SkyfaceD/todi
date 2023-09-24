package org.skyfaced.noti.util.exception

import org.skyfaced.noti.settings.NotiLocale
import org.skyfaced.noti.settings.Settings
import org.skyfaced.noti.settings.strings

class UnexpectedException(
    locale: Settings<NotiLocale>
) : ResourceException(locale.strings.msg_unexpected_exception)