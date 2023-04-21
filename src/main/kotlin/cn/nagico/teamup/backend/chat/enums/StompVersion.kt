package cn.nagico.teamup.backend.chat.enums

import io.netty.util.AttributeKey
import io.netty.util.internal.StringUtil

/**
 * STOMP protocol version
 *
 * @property version
 * @property subProtocol
 */
enum class StompVersion(val version: String, val subProtocol: String) {
    STOMP_V11("1.1", "v11.stomp"),
    STOMP_V12("1.2", "v12.stomp");

    companion object {
        val CHANNEL_ATTRIBUTE_KEY: AttributeKey<StompVersion> = AttributeKey.valueOf("stomp_version")
        var SUB_PROTOCOLS: String

        init {
            val subProtocols: MutableList<String> = ArrayList(values().size)
            for (stompVersion in values()) {
                subProtocols.add(stompVersion.subProtocol)
            }
            SUB_PROTOCOLS = StringUtil.join(",", subProtocols).toString()
        }

        fun findBySubProtocol(subProtocol: String): StompVersion {
            for (stompVersion in values()) {
                if (stompVersion.subProtocol == subProtocol) {
                    return stompVersion
                }
            }
            throw IllegalArgumentException("Not found StompVersion for '$subProtocol'")
        }
    }
}