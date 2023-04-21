package cn.nagico.teamup.utils.jwt

import cn.nagico.teamup.utils.jwt.entity.JwtPayload
import cn.nagico.teamup.utils.jwt.entity.TokenType
import cn.nagico.teamup.backend.util.jwt.exception.ParseError
import cn.nagico.teamup.backend.util.jwt.exception.ValidateError
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.MACVerifier


object JwtUtils {
    private const val SECRET = "tLOPZTsVizsB6FLn24T00gQwYYzgBDKeSL7QzTaYgO1dN1txjFjbjoVXDTcTBb2i"
    private val VERIFIER = MACVerifier(SECRET)

    /**
     * 验证token
     *
     * @param token
     * @return JwtPayload
     */
    fun validateToken(token: String): JwtPayload {
        val jwsObject = JWSObject.parse(token)!!
        if (!jwsObject.verify(VERIFIER)) {
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