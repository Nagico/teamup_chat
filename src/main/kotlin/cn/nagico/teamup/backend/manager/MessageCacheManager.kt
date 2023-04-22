package cn.nagico.teamup.backend.manager


import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.constant.RedisKey
import cn.nagico.teamup.backend.enums.StompMessageType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.UUID


@Component
class MessageCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun getMessageCache(messageId: String, type: StompMessageType): StompMessage? {
        return redisTemplate.opsForValue()[RedisKey.messageKey(messageId, type)] as? StompMessage
    }

    fun getMessageCacheByKey(key: String): StompMessage? {
        return redisTemplate.opsForValue()[key] as? StompMessage
    }

    fun setMessageCache(message: StompMessage) {
        redisTemplate.opsForValue().set(
            RedisKey.messageKey(message.id, message.type),
            message
        )
    }

    fun deleteMessageCache(messageId: String, type: StompMessageType) {
        redisTemplate.delete(RedisKey.messageKey(messageId, type))
    }

    fun deleteMessageCacheByKey(key: String) {
        redisTemplate.delete(key)
    }
}