package cn.nagico.teamup.backend.exception.frame

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType

class StompHeadMissing (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.HEADER_MISSING, message, cause)