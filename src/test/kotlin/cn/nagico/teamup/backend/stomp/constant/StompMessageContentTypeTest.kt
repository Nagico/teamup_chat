package cn.nagico.teamup.backend.stomp.constant

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class StompMessageContentTypeTest {

    @Test
    fun of() {
        assertEquals(StompMessageContentType.CHAT, StompMessageContentType.of(1))
        assertEquals(StompMessageContentType.READ, StompMessageContentType.of(2))
    }
}