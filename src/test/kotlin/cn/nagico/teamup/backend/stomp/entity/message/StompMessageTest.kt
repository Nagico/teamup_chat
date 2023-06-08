package cn.nagico.teamup.backend.stomp.entity.message

import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class StompMessageTest {

    @Test
    fun toStompFrame_ack() {
        val stompMessage  = StompMessage(
            id = "messageId",
            type = StompMessageType.ACK,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.READ, "readId"),
            createTime = 1L
        )

        val stompFrame = stompMessage.toStompFrame()

        assertEquals("ACK", stompFrame.command().name)
        assertEquals("messageId", stompFrame.headers()["message-id"])
    }
}