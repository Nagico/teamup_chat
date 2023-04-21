package cn.nagico.teamup.backend.chat.enums

import io.netty.handler.codec.stomp.StompCommand

enum class StompDataType(val stompCommand: StompCommand) {
    MESSAGE(StompCommand.MESSAGE),
    ACK(StompCommand.ACK),
    ;

    companion object {
        fun of(stompCommand: StompCommand): StompDataType {
            return values().firstOrNull { it.stompCommand == stompCommand } ?: MESSAGE
        }

        fun of(stompCommand: String): StompDataType {
            return values().firstOrNull { it.stompCommand.name == stompCommand } ?: MESSAGE
        }
    }
}