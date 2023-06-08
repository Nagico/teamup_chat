package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.cache.MessageCacheManager
import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.service.MessageQueueService
import cn.nagico.teamup.backend.service.UserService
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StompMessageServiceImplTest {
    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var messageCacheManager: MessageCacheManager

    @Mock
    private lateinit var messageQueueService: MessageQueueService

    @Mock
    private lateinit var userCacheManager: UserCacheManager

    @InjectMocks
    private lateinit var stompMessageService: StompMessageServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testGetMessage() {
        val messageId = "example-message-id"
        val expectedStompMessage = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        Mockito.`when`(messageCacheManager.getMessageCache(messageId)).thenReturn(expectedStompMessage)

        val result = stompMessageService.getMessage(messageId)

        assertEquals(expectedStompMessage, result)
        Mockito.verify(messageCacheManager).getMessageCache(messageId)
    }

    @Test
    fun testDeliverMessage() {
        val message = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )
        val target = "example-target"

        Mockito.`when`(userService.getUserServer(message.receiver)).thenReturn(target)

        stompMessageService.deliverMessage(message)

        Mockito.verify(messageCacheManager).addMessageCache(message)
        Mockito.verify(userCacheManager).addUserUnreceivedMessage(message)
        Mockito.verify(messageQueueService).saveStompMessage(message)
        Mockito.verify(messageQueueService).forwardStompMessage(target, message)
    }

    @Test
    fun testAckMessage() {
        val userId = 123L
        val messageId = "example-message-id"

        stompMessageService.ackMessage(userId, messageId)

        Mockito.verify(userCacheManager).deleteUserUnreceivedMessage(userId, messageId)
        Mockito.verify(messageCacheManager).deleteMessageCache(messageId)
    }

    @Test
    fun testFetchUnreceivedMessages() {
        val userId = 123L
        val unreceivedMessageIds = listOf("message-id-1", "message-id-2")
        val expectedStompMessages = listOf(
            StompMessage(
                id = "message-id-1",
                type = StompMessageType.MESSAGE,
                sender = 2L,
                receiver = 1L,
                content = StompMessageContent(StompMessageContentType.CHAT, "content"),
                createTime = 1L
            ),
            StompMessage(
                id = "message-id-2",
                type = StompMessageType.MESSAGE,
                sender = 2L,
                receiver = 1L,
                content = StompMessageContent(StompMessageContentType.CHAT, "content"),
                createTime = 2L
            )
        )

        Mockito.`when`(userCacheManager.getUserUnreceivedMessages(userId)).thenReturn(unreceivedMessageIds)
        Mockito.`when`(messageCacheManager.getMessageCache("message-id-1")).thenReturn(expectedStompMessages[0])
        Mockito.`when`(messageCacheManager.getMessageCache("message-id-2")).thenReturn(expectedStompMessages[1])

        val result = stompMessageService.fetchUnreceivedMessages(userId)

        assertEquals(expectedStompMessages, result)
        Mockito.verify(userCacheManager).getUserUnreceivedMessages(userId)
        Mockito.verify(messageCacheManager).getMessageCache("message-id-1")
        Mockito.verify(messageCacheManager).getMessageCache("message-id-2")
    }
}