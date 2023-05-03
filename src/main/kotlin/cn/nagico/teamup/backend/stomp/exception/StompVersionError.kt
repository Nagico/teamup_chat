package cn.nagico.teamup.backend.stomp.exception

import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompExceptionType

class StompVersionError (
    exceptedVersion: String,
    cause: Throwable? = null
): StompException(StompExceptionType.INVALID_VERSION, "Received invalid version, expected $exceptedVersion", cause)