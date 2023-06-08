package cn.nagico.teamup.backend.cache.impl

import cn.nagico.teamup.backend.cache.constant.RedisKey
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import jakarta.annotation.Resource
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
class MessageCacheManagerImplTest {
    @Resource
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var messageCacheManagerImpl: MessageCacheManagerImpl

    @BeforeEach
    fun setUp() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

    @AfterEach
    fun tearDown() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

    @Test
    fun getMessageCache() {
        val userId = 1L
        val messageId = "messageId"

        val message = StompMessage(
            id = messageId,
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = userId,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        redisTemplate.opsForValue().set(
            RedisKey.messageKey(message.id),
            message
        )

        assertEquals(message, messageCacheManagerImpl.getMessageCache(messageId))
    }

    @Test
    fun addMessageCache() {
        val userId = 1L
        val messageId = "messageId"

        val message = StompMessage(
            id = messageId,
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = userId,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        messageCacheManagerImpl.addMessageCache(message)

        assertEquals(message, redisTemplate.opsForValue()[RedisKey.messageKey(message.id)])
    }

    @Test
    fun deleteMessageCache() {
        val userId = 1L
        val messageId = "messageId"

        val message = StompMessage(
            id = messageId,
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = userId,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        redisTemplate.opsForValue().set(
            RedisKey.messageKey(message.id),
            message
        )

        messageCacheManagerImpl.deleteMessageCache(messageId)

        assertNull(redisTemplate.opsForValue()[RedisKey.messageKey(message.id)])
    }
}