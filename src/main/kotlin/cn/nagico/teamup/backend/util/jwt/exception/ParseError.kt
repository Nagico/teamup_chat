package cn.nagico.teamup.backend.util.jwt.exception

class ParseError : JwtException {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}