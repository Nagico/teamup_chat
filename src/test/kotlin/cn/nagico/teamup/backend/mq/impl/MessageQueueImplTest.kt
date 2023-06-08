package cn.nagico.teamup.backend.mq.impl

import cn.nagico.teamup.backend.service.StompService
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import com.alibaba.fastjson.JSON
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MessageQueueImplTest {
    @Mock
    private lateinit var stompService: StompService

    @InjectMocks
    private lateinit var messageQueueImpl: MessageQueueImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun handleDeliverMessage() {
        val message = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        messageQueueImpl.handleDeliverMessage(JSON.toJSONString(message))

        Mockito.verify(stompService).deliverMessage(message)
    }
}