package cn.nagico.teamup.backend.manager

import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class MessageQueueManagerTest {
    @Autowired
    lateinit var messageQueueManager: MessageQueueManager

    @Test
    fun sendStompMessage() {
        val newMessage = StompMessage(
            id = UUID.randomUUID(),
            content = "test",
            type = StompMessageType.MESSAGE,
            sender = 2,
            receiver = 3,
            createTime = 4,
        )
        messageQueueManager.saveStompMessage(newMessage)
    }
}