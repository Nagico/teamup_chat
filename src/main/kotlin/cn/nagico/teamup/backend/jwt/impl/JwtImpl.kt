package cn.nagico.teamup.backend.jwt.impl

import cn.nagico.teamup.backend.jwt.Jwt
import cn.nagico.teamup.backend.jwt.entity.JwtPayload
import cn.nagico.teamup.backend.jwt.constant.TokenType
import cn.nagico.teamup.backend.jwt.exception.ParseError
import cn.nagico.teamup.backend.jwt.exception.ValidateError
import cn.nagico.teamup.backend.util.properties.JwtProperties
import cn.nagico.teamup.backend.util.annotation.Business
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.MACVerifier
import org.springframework.beans.factory.annotation.Autowired


@Business
class JwtImpl: Jwt {
    @Autowired
    private lateinit var jwtProperties: JwtProperties

    private lateinit var jwtVerifier: MACVerifier

    /**
     * 验证token
     *
     * @param token
     * @return JwtPayload
     */
    override fun validateToken(token: String): JwtPayload {
        if (!this::jwtVerifier.isInitialized) {
            jwtVerifier = MACVerifier(jwtProperties.secret)
        }

        val jwsObject = JWSObject.parse(token)!!
        if (!jwsObject.verify(jwtVerifier)) {
            throw ValidateError("Token is invalid")
        }

        try {
            val payload = jwsObject.payload.toJSONObject()
            val tokenType = TokenType.valueOf((payload["token_type"] as String).uppercase())
            val userId = payload["user_id"] as Long
            return JwtPayload(tokenType, userId)
        }
        catch (e: Exception) {
            throw ParseError("Token is invalid", e)
        }

    }
}