package cn.nagico.teamup.backend.stomp.constant

enum class StompMessageContentType(val value: Int) {
    UNKNOWN(0),
    CHAT(1),
    READ(2),
    ;

    companion object {
        fun of(value: Int): StompMessageContentType {
            return values().firstOrNull { it.value == value } ?: CHAT
        }
    }
}