package cn.nagico.teamup.backend.stomp.constant

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class StompAckModeTest {

    @Test
    fun testToString() {
        assertEquals("auto", StompAckMode.AUTO.toString())
        assertEquals("client", StompAckMode.CLIENT.toString())
        assertEquals("client-individual", StompAckMode.CLIENT_INDIVIDUAL.toString())
    }

    @Test
    fun isAuto() {
        assertEquals(true, StompAckMode.AUTO.isAuto)
        assertEquals(false, StompAckMode.CLIENT.isAuto)
        assertEquals(false, StompAckMode.CLIENT_INDIVIDUAL.isAuto)
    }

    @Test
    fun getNeedAck() {
        assertEquals(false, StompAckMode.AUTO.needAck)
        assertEquals(true, StompAckMode.CLIENT.needAck)
        assertEquals(true, StompAckMode.CLIENT_INDIVIDUAL.needAck)
    }

    @Test
    fun getMode() {
        assertEquals("auto", StompAckMode.AUTO.mode)
        assertEquals("client", StompAckMode.CLIENT.mode)
        assertEquals("client-individual", StompAckMode.CLIENT_INDIVIDUAL.mode)
    }
}