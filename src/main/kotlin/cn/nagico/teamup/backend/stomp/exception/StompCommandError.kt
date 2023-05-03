package cn.nagico.teamup.backend.stomp.exception

import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompExceptionType

class StompCommandError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.UNSUPPORTED_COMMAND, message, cause)