package org.skyfaced.noti.util.exception

import org.skyfaced.noti.R

class FlowException(
    message: String? = null,
) : ResourceException(R.string.msg_flow_exception, message)