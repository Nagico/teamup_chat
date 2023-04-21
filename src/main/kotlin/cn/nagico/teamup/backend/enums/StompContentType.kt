package cn.nagico.teamup.backend.enums

enum class StompContentType(val contentType: String) {
    TEXT("text/plain"),
    JSON("application/json"),
    XML("application/xml"),
    HTML("text/html"),
    FORM("application/x-www-form-urlencoded"),
    MULTIPART("multipart/form-data"),
    OCTET("application/octet-stream"),
    ;

    companion object {
        fun of(contentType: String): StompContentType {
            return values().firstOrNull { it.contentType == contentType } ?: TEXT
        }
    }

    override fun toString(): String {
        return contentType
    }
}