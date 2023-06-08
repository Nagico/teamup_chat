package cn.nagico.teamup.backend.stomp.constant

import io.netty.handler.codec.stomp.StompCommand
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StompMessageTypeTest {
    @Test
    fun ofStompCommand() {
        assertEquals(StompMessageType.MESSAGE, StompMessageType.of(StompCommand.STOMP))
        assertEquals(StompMessageType.ACK, StompMessageType.of(StompCommand.ACK))
    }

    @Test
    fun ofStompString() {
        assertEquals(StompMessageType.MESSAGE, StompMessageType.of("MESSAGE"))
        assertEquals(StompMessageType.ACK, StompMessageType.of("ACK"))
    }

    @Test
    fun ofInt() {
        assertEquals(StompMessageType.MESSAGE, StompMessageType.of(1))
        assertEquals(StompMessageType.ACK, StompMessageType.of(2))
    }
}