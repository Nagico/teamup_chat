package cn.nagico.teamup.backend.util.jwt.exception

import cn.nagico.teamup.backend.exception.StompAuthError

open class JwtException (
    message: String? = null,
    cause: Throwable? = null
): StompAuthError(message, cause)