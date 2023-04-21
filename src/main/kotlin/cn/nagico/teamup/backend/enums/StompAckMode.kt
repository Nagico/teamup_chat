package cn.nagico.teamup.backend.enums

enum class StompAckMode(val mode: String) {
    AUTO("auto"),
    CLIENT("client"),
    CLIENT_INDIVIDUAL("client-individual"),
    ;

    companion object {
        fun of(mode: String): StompAckMode {
            return values().firstOrNull { it.mode == mode } ?: AUTO
        }
    }

    override fun toString(): String {
        return mode
    }

    val isAuto: Boolean
        get() = this == AUTO

    val needAck: Boolean
        get() = this != AUTO
}