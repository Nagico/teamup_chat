package cn.nagico.teamup.backend.util

import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class UUIDUtilTest {

    @Test
    fun testFromHex() {
        val hex = "0b7d5ddf2dd44c31a8d64764ebf2e2f0"
        val uuid = UUIDUtil.fromHex(hex)
        assertEquals(hex, uuid.toString().replace("-", ""))
    }
}