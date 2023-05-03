package cn.nagico.teamup.backend.stomp.exception

import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompExceptionType

open class StompAuthError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.AUTHENTICATION_ERROR, message, cause)