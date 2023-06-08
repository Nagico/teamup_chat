package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import cn.nagico.teamup.backend.util.config.RabbitMQConfig
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MessageQueueServiceImplTest {
    @Mock
    private lateinit var rabbitTemplate: RabbitTemplate

    @InjectMocks
    private lateinit var messageQueueService: MessageQueueServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testSaveStompMessage() {
        val stompMessage  = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )
        val json = stompMessage.toJson()

        messageQueueService.saveStompMessage(stompMessage)

        Mockito.verify(rabbitTemplate).convertAndSend(
            RabbitMQConfig.SAVE_EXCHANGE_NAME,
            "",
            json
        )
    }

    @Test
    fun testForwardStompMessage() {
        val target = "example.target"
        val stompMessage  = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )
        val json = stompMessage.toJson()

        messageQueueService.forwardStompMessage(target, stompMessage)

        Mockito.verify(rabbitTemplate).convertAndSend(
            RabbitMQConfig.DELIVER_EXCHANGE_NAME,
            target,
            json
        )
    }

}