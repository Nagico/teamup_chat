package cn.nagico.teamup.backend.util

import java.math.BigInteger
import java.util.*


object UUIDUtil {
    fun fromHex(hex: String): UUID {
        val bi1 = BigInteger(hex.substring(0, 16), 16)
        val bi2 = BigInteger(hex.substring(16, 32), 16)
        return UUID(bi1.toLong(), bi2.toLong())
    }

    fun toHex(uuid: UUID): String {
        return uuid.toString().replace("-", "")
    }
}