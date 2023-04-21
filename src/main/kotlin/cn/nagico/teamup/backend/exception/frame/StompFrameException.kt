package cn.nagico.teamup.backend.exception.frame

import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType

class StompFrameException (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.FRAME_ERROR, message, cause)