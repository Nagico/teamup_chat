package cn.nagico.teamup.backend.util.properties

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NettyPropertiesTest {
    @Autowired
    private lateinit var nettyProperties: NettyProperties

    @Test
    fun getProperties() {
        assertEquals(1, nettyProperties.boss)
        assertEquals(1, nettyProperties.worker)
        assertEquals(8060, nettyProperties.port)
        assertEquals(18060, nettyProperties.portSalve)
        assertEquals(6000, nettyProperties.timeout)
    }
}