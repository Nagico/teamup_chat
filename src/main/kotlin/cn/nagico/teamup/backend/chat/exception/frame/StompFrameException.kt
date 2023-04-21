package cn.nagico.teamup.backend.chat.exception.frame

import cn.nagico.teamup.backend.chat.exception.StompException
import cn.nagico.teamup.backend.chat.exception.StompExceptionType

class StompFrameException (
    message: String? = null,
    cause: Throwable? = null
): StompException(StompExceptionType.FRAME_ERROR, message, cause)