package cn.nagico.teamup.backend.chat.service

import cn.nagico.teamup.backend.chat.entity.StompMessage
import cn.nagico.teamup.backend.chat.enums.StompMessageType
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertEquals

@SpringBootTest
class StompMessageServiceTest {
    @Autowired
    private lateinit var stompMessageService: StompMessageService

    @Test
    fun getMessage() {
        val id = UUIDUtil.fromHex("0b7d5ddf2dd44c31a8d64764ebf2e2f0")
        val message = stompMessageService.getMessage(id)
        assertEquals("getMessage", id, message.id)
    }

    @Test
    fun setMessage() {
        val id = UUIDUtil.fromHex("0b7d5ddf2dd44c31a8d64764ebf2e2f0")
        val message = stompMessageService.getMessage(id)
        assertEquals("getMessage", id, message.id)
        val newMessage = StompMessage(
            id = id,
            content = "test",
            type = StompMessageType.MESSAGE,
            sender = 2,
            receiver = 3,
            createTime = 4,
        )
        stompMessageService.setMessage(id, newMessage)
        val message2 = stompMessageService.getMessage(id)
        assertEquals("setMessage", newMessage.content, message2.content)
    }
}