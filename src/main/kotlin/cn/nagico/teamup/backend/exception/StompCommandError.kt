package cn.nagico.teamup.backend.exception

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType

class StompCommandError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.UNSUPPORTED_COMMAND, message, cause)