package cn.nagico.teamup.backend.stomp.exception

class StompVersionError (
    exceptedVersion: String,
    cause: Throwable? = null
): StompException(StompExceptionType.INVALID_VERSION, "Received invalid version, expected $exceptedVersion", cause)