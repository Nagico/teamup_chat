package cn.nagico.teamup.backend.stomp.entity.message

import cn.nagico.teamup.backend.stomp.constant.StompContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.exception.frame.StompHeadMissing
import com.alibaba.fastjson.JSON
import io.netty.buffer.Unpooled
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import java.io.Serializable

data class StompMessage(
    val id: String,
    val type: StompMessageType,
    val sender: Long,
    val receiver: Long,
    val content: StompMessageContent,
    val createTime: Long,
): Serializable {
    constructor(frame: StompFrame, sender: Long, receiver: Long) : this(
        id = frame.headers().getAsString(StompHeaders.ID) ?: throw StompHeadMissing("id"),
        type = StompMessageType.of(frame.command()),
        sender = sender,
        receiver = receiver,
        content = JSON.parseObject(frame.content().toString(Charsets.UTF_8), StompMessageContent::class.java),
        createTime = System.currentTimeMillis(),
    )

    fun toStompFrame(): StompFrame {
        return when (type) {
            StompMessageType.MESSAGE -> {
                DefaultStompFrame(StompMessageType.MESSAGE.stompCommand, Unpooled.copiedBuffer(content.toString(), Charsets.UTF_8)).apply {
                    headers()
                        .set(StompHeaders.ID, id)
                        .set(StompHeaders.SUBSCRIPTION, sender.toString())
                        .set(StompHeaders.DESTINATION, receiver.toString())
                        .set("create-time", createTime.toString())
                        .set(StompHeaders.CONTENT_TYPE, StompContentType.JSON.contentType)
                        .set(StompHeaders.CONTENT_LENGTH, (content.toString().length).toString())
                }
            }
            StompMessageType.ACK -> {
                DefaultStompFrame(StompMessageType.ACK.stompCommand).apply {
                    headers()
                        .set(StompHeaders.MESSAGE_ID, id)
                }
            }
        }
    }

    fun toJson(): String {
        return JSON.toJSONString(this)
    }

}
