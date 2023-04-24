package cn.nagico.teamup.backend.enums

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