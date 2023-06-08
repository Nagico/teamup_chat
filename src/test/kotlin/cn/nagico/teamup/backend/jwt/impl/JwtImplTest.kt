package cn.nagico.teamup.backend.jwt.impl

import cn.nagico.teamup.backend.jwt.exception.ParseError
import cn.nagico.teamup.backend.jwt.exception.ValidateError
import cn.nagico.teamup.backend.util.properties.JwtProperties
import com.nimbusds.jose.*
import com.nimbusds.jose.crypto.MACSigner
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class JwtImplTest {
    @Autowired
    private lateinit var jwtProperties: JwtProperties

    @Autowired
    lateinit var jwtImpl: JwtImpl

    private fun generateJWT(payloadMap: Map<String, Any>, secret: String): String {
        val jwsHeader = JWSHeader(JWSAlgorithm.HS256)

        val payload = Payload(payloadMap)

        val jwsObject = JWSObject(jwsHeader, payload)

        try {
            val signer = MACSigner(secret.toByteArray())
            jwsObject.sign(signer)
        } catch (e: JOSEException) {
            e.printStackTrace()
        }
        return jwsObject.serialize()
    }

    @Test
    fun validateToken() {
        val userId = 1L
        val type = "access"
        val token = generateJWT(mapOf("user_id" to userId, "token_type" to type), jwtProperties.secret)
        val payload = jwtImpl.validateToken(token)
        assertEquals(userId, payload.userId)
        assertEquals(type.uppercase(), payload.tokenType.name)
    }

    @Test
    fun validateToken_wrongSecret() {
        val userId = 1L
        val type = "access"
        val token = generateJWT(mapOf("user_id" to userId, "token_type" to type), jwtProperties.secret + "wrong secret")
        assertThrows(ValidateError::class.java) {
            jwtImpl.validateToken(token)
        }
    }

    @Test
    fun validateToken_wrongType() {
        val userId = 1L
        val type = "access2"
        val token = generateJWT(mapOf("user_id" to userId, "token_type" to type), jwtProperties.secret)
        assertThrows(ParseError::class.java) {
            jwtImpl.validateToken(token)
        }
    }
}