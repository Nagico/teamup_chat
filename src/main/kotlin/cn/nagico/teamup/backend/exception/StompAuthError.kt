package cn.nagico.teamup.backend.exception

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType

open class StompAuthError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.AUTHENTICATION_ERROR, message, cause)