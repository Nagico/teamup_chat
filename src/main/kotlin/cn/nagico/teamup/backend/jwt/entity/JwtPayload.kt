package cn.nagico.teamup.backend.jwt.entity

import cn.nagico.teamup.backend.jwt.constant.TokenType

data class JwtPayload(
    val tokenType: TokenType,
    val userId: Long,
)
