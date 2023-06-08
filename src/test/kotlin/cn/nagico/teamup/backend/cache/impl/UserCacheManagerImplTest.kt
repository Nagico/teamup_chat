package cn.nagico.teamup.backend.cache.impl

import cn.nagico.teamup.backend.cache.constant.RedisKey
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import jakarta.annotation.Resource
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate


@SpringBootTest
class UserCacheManagerImplTest {
    @Resource
    lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var userCacheManagerImpl: UserCacheManagerImpl

    @BeforeEach
    fun setUp() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

    @AfterEach
    fun tearDown() {
        redisTemplate.connectionFactory?.connection?.serverCommands()?.flushAll()
    }

    @Test
    fun getUserServer() {
        val userId = 1L
        val serverUUID = "serverUUID"
        redisTemplate.opsForValue().set(RedisKey.userServerKey(userId), serverUUID)
        assertEquals(serverUUID, userCacheManagerImpl.getUserServer(userId))
    }

    @Test
    fun online() {
        val userId = 1L
        val serverUUID = "serverUUID"
        userCacheManagerImpl.online(userId, serverUUID)
        assertEquals(serverUUID, userCacheManagerImpl.getUserServer(userId))
    }

    @Test
    fun offline() {
        val userId = 1L
        val serverUUID = "serverUUID"
        userCacheManagerImpl.online(userId, serverUUID)
        userCacheManagerImpl.offline(userId)
        assertNull(userCacheManagerImpl.getUserServer(userId))
    }

    @Test
    fun getUserUnreceivedMessages() {
        val userId = 1L
        val messageId1 = "messageId1"
        val messageId2 = "messageId2"

        redisTemplate.opsForList().rightPush(RedisKey.userUnreceivedMessagesKey(userId), messageId1)
        redisTemplate.opsForList().rightPush(RedisKey.userUnreceivedMessagesKey(userId), messageId2)

        assertEquals(listOf(messageId1, messageId2), userCacheManagerImpl.getUserUnreceivedMessages(userId))
    }

    @Test
    fun addUserUnreceivedMessage() {
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

        userCacheManagerImpl.addUserUnreceivedMessage(message)
        assertEquals(listOf(messageId), userCacheManagerImpl.getUserUnreceivedMessages(userId))
    }

    @Test
    fun deleteUserUnreceivedMessage() {
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

        userCacheManagerImpl.addUserUnreceivedMessage(message)
        userCacheManagerImpl.deleteUserUnreceivedMessage(userId, messageId)
        assertEquals(listOf<String>(), userCacheManagerImpl.getUserUnreceivedMessages(userId))
    }
}