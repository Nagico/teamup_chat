package cn.nagico.teamup.backend.cache


import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class MessageCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    fun getMessageCache(messageId: String): StompMessage? {
        return redisTemplate.opsForValue()[cn.nagico.teamup.backend.cache.constant.RedisKey.messageKey(messageId)] as? StompMessage
    }

    fun addMessageCache(message: StompMessage) {
        redisTemplate.opsForValue().set(
            cn.nagico.teamup.backend.cache.constant.RedisKey.messageKey(message.id),
            message
        )
    }

    fun deleteMessageCache(messageId: String) {
        redisTemplate.delete(cn.nagico.teamup.backend.cache.constant.RedisKey.messageKey(messageId))
    }
}