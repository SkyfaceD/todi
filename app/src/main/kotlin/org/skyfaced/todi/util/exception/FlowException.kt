package org.skyfaced.todi.util.exception

import org.skyfaced.todi.R

class FlowException(
    message: String? = null,
) : ResourceException(R.string.msg_flow_exception, message)