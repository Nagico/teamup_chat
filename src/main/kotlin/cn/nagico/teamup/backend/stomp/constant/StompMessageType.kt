package cn.nagico.teamup.backend.stomp.constant

import io.netty.handler.codec.stomp.StompCommand

enum class StompMessageType(val stompCommand: StompCommand, val value: Int) {
    MESSAGE(StompCommand.MESSAGE, 1),
    ACK(StompCommand.ACK, 2),
    ;

    companion object {
        fun of(stompCommand: StompCommand): StompMessageType {
            return values().firstOrNull { it.stompCommand == stompCommand } ?: MESSAGE
        }

        fun of(stompCommand: String): StompMessageType {
            return values().firstOrNull { it.stompCommand.name == stompCommand } ?: MESSAGE
        }

        fun of(value: Int): StompMessageType {
            return values().firstOrNull { it.value == value } ?: MESSAGE
        }
    }
}