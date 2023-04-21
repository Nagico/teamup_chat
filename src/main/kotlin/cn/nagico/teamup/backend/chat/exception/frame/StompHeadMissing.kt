package cn.nagico.teamup.backend.chat.exception.frame

import cn.nagico.teamup.backend.chat.exception.StompException
import cn.nagico.teamup.backend.chat.exception.StompExceptionType

class StompHeadMissing (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.HEADER_MISSING, message, cause)