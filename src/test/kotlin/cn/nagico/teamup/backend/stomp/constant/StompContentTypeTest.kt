package cn.nagico.teamup.backend.stomp.constant

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StompContentTypeTest {
    @Test
    fun of() {
        assertEquals(StompContentType.TEXT, StompContentType.of("text/plain"))
    }

    @Test
    fun toStringTest() {
        assertEquals("text/plain", StompContentType.TEXT.toString())
    }
}