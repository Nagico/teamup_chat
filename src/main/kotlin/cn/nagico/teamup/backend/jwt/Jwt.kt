package cn.nagico.teamup.backend.jwt

import cn.nagico.teamup.backend.jwt.entity.JwtPayload

interface Jwt {
    /**
     * 验证token
     *
     * @param token
     * @return JwtPayload
     */
    fun validateToken(token: String): JwtPayload
}