package cn.nagico.teamup.backend.util.jwt.exception

open class JwtException : RuntimeException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}