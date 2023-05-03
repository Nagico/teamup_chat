package cn.nagico.teamup.backend.manager

import cn.nagico.teamup.backend.service.MessageQueueService
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class MessageQueueManagerTest {
    @Autowired
    lateinit var messageQueueService: MessageQueueService

    @Test
    fun sendStompMessage() {
        val newMessage = StompMessage(
            id = UUID.randomUUID().toString(),
            content = StompMessageContent(StompMessageContentType.CHAT, "123"),
            type = StompMessageType.MESSAGE,
            sender = 2,
            receiver = 3,
            createTime = 4,
        )
        messageQueueService.saveStompMessage(newMessage)
    }
}