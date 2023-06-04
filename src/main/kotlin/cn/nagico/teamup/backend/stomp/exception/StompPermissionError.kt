package cn.nagico.teamup.backend.stomp.exception

open class StompPermissionError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.PERMISSION_ERROR, message, cause)