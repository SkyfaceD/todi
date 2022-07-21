package org.skyfaced.todi.util.exception

import org.skyfaced.todi.R

class UnexpectedException(
    message: String? = null,
) : ResourceException(R.string.msg_unexpected_exception, message)