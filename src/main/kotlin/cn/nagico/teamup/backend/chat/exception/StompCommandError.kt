package cn.nagico.teamup.backend.chat.exception

import cn.nagico.teamup.backend.chat.exception.StompException
import cn.nagico.teamup.backend.chat.exception.StompExceptionType

class StompCommandError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.UNSUPPORTED_COMMAND, message, cause)