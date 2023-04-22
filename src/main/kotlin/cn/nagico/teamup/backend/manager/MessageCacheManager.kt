package cn.nagico.teamup.backend.manager


import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.constant.RedisKey
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.UUID


@Component
class MessageCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun getMessageCache(messageId: String): StompMessage? {
        return redisTemplate.opsForValue()[RedisKey.messageKey(messageId)] as? StompMessage
    }

    fun setMessageCache(message: StompMessage) {
        redisTemplate.opsForValue().set(
            RedisKey.messageKey(message.id),
            message
        )
    }

    fun deleteMessageCache(messageId: String) {
        redisTemplate.delete(RedisKey.messageKey(messageId))
    }
}