package cn.nagico.teamup.backend.exception

enum class StompExceptionType(val content: String) {
    FRAME_ERROR("frame_error"),
    HEADER_MISSING("header_missing"),
    INVALID_VERSION("invalid_version"),
    UNSUPPORTED_COMMAND("unsupported_command"),
    AUTHENTICATION_ERROR("authentication_error"),
    UNKNOWN_ERROR("unknown_error"),
}