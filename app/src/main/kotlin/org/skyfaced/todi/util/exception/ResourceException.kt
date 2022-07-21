package org.skyfaced.todi.util.exception

import androidx.annotation.StringRes

open class ResourceException(
    @StringRes
    val messageRes: Int,
    message: String? = null,
) : Exception(message)