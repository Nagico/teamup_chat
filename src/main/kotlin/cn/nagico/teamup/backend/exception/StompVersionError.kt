package cn.nagico.teamup.backend.exception

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType

class StompVersionError (
    exceptedVersion: String,
    cause: Throwable? = null
): StompException(StompExceptionType.INVALID_VERSION, "Received invalid version, expected $exceptedVersion", cause)