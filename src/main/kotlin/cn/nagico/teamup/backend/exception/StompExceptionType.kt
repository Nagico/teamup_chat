package cn.nagico.teamup.backend.exception

enum class StompExceptionType(val content: String) {
    FRAME_ERROR("frame_error"),
    HEADER_MISSING("header_missing"),
    UNSUPPORTED_COMMAND("unsupported_command"),
    UNKNOWN_ERROR("unknown_error"),
}