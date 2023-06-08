package cn.nagico.teamup.backend.util.properties

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtPropertiesTest {
    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @Test
    fun getSecret() {
        assertEquals("secretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecretsecret", jwtProperties.secret)
    }
}