package cn.nagico.teamup.utils.jwt.entity

enum class TokenType(val value: String) {
    ACCESS("access"),
    REFRESH("refresh");
}