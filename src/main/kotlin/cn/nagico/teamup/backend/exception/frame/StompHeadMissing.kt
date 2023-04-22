package cn.nagico.teamup.backend.exception.frame

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType
import io.netty.util.AsciiString

class StompHeadMissing (
    header: String,
    cause: Throwable? = null
): StompException(StompExceptionType.HEADER_MISSING, "Required '$header' header missed", cause) {
    constructor(header: AsciiString, cause: Throwable? = null): this(header.toString(), cause)
}