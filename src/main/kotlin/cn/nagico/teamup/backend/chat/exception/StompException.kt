package cn.nagico.teamup.backend.chat.exception

open class StompException(
    val type: StompExceptionType = StompExceptionType.UNKNOWN_ERROR,
    message: String? = null,
    cause: Throwable? = null
): RuntimeException(message, cause)