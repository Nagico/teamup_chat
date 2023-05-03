package cn.nagico.teamup.backend.jwt.exception

import cn.nagico.teamup.backend.stomp.exception.StompAuthError

open class JwtException (
    message: String? = null,
    cause: Throwable? = null
): StompAuthError(message, cause)