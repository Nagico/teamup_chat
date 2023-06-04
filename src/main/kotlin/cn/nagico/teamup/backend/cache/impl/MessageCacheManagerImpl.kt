package cn.nagico.teamup.backend.cache.impl


import cn.nagico.teamup.backend.cache.MessageCacheManager
import cn.nagico.teamup.backend.cache.constant.RedisKey
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class MessageCacheManagerImpl: MessageCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    override fun getMessageCache(messageId: String): StompMessage? {
        return redisTemplate.opsForValue()[RedisKey.messageKey(messageId)] as? StompMessage
    }

    override fun addMessageCache(message: StompMessage) {
        redisTemplate.opsForValue().set(
            RedisKey.messageKey(message.id),
            message
        )
    }

    override fun deleteMessageCache(messageId: String) {
        redisTemplate.delete(RedisKey.messageKey(messageId))
    }
}