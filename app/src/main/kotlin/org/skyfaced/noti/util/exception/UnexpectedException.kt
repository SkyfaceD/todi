package org.skyfaced.noti.util.exception

import org.skyfaced.noti.R

class UnexpectedException(
    message: String? = null,
) : ResourceException(R.string.msg_unexpected_exception, message)