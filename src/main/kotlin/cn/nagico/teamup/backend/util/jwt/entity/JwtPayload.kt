package cn.nagico.teamup.utils.jwt.entity

data class JwtPayload(
    val tokenType: TokenType,
    val userId: Long,
)
