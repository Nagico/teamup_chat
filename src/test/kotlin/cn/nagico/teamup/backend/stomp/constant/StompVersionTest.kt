package cn.nagico.teamup.backend.stomp.constant

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class StompVersionTest {
    @Test
    fun findBySubProtocol() {
        assertEquals(StompVersion.STOMP_V11, StompVersion.findBySubProtocol("v11.stomp"))
        assertEquals(StompVersion.STOMP_V12, StompVersion.findBySubProtocol("v12.stomp"))
    }

    @Test
    fun findBySubProtocol_notExist() {
        assertThrows(IllegalArgumentException::class.java) {
            StompVersion.findBySubProtocol("v13.stomp")
        }
    }
}