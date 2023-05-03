package cn.nagico.teamup.backend.stomp.exception

open class StompAuthError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.AUTHENTICATION_ERROR, message, cause)