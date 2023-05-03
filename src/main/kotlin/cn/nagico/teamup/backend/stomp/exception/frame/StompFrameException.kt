package cn.nagico.teamup.backend.stomp.exception.frame

import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompExceptionType

class StompFrameException (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.FRAME_ERROR, message, cause)