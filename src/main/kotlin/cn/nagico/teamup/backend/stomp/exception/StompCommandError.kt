package cn.nagico.teamup.backend.stomp.exception

class StompCommandError (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.UNSUPPORTED_COMMAND, message, cause)