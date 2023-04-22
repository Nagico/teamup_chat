package cn.nagico.teamup.backend.entity

import cn.nagico.teamup.backend.enums.StompContentType
import cn.nagico.teamup.backend.enums.StompMessageType
import cn.nagico.teamup.backend.exception.frame.StompHeadMissing
import cn.nagico.teamup.backend.model.Message
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import com.alibaba.fastjson.JSON
import io.netty.buffer.Unpooled
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import java.io.Serializable
import java.util.UUID

data class StompMessage(
    val id: String,
    val type: StompMessageType,
    val sender: Long,
    val receiver: Long,
    val content: String?,
    val createTime: Long,
): Serializable {
    constructor(frame: StompFrame, sender: Long, receiver: Long) : this(
        id = frame.headers().getAsString(StompHeaders.ID) ?: throw StompHeadMissing("id"),
        type = StompMessageType.of(frame.command()),
        sender = sender,
        receiver = receiver,
        content = frame.content().toString(Charsets.UTF_8),
        createTime = System.currentTimeMillis(),
    )

    constructor(message: Message) : this(
        id = message.id!!,
        type = StompMessageType.of(message.type!!),
        sender = message.senderId!!,
        receiver = message.receiverId!!,
        content = message.content,
        createTime = message.createTime!!.toInstant(
            java.time.ZoneOffset.of("+8")
        ).toEpochMilli(),
    )

    fun toStompFrame(): StompFrame {
        return when (type) {
            StompMessageType.MESSAGE -> {
                DefaultStompFrame(StompMessageType.MESSAGE.stompCommand, Unpooled.copiedBuffer(content, Charsets.UTF_8)).apply {
                    headers()
                        .set(StompHeaders.ID, id)
                        .set(StompHeaders.CONTENT_TYPE, StompContentType.JSON.contentType)
                        .set(StompHeaders.CONTENT_LENGTH, (content?.length ?: 0).toString())
                }
            }
            StompMessageType.ACK -> {
                DefaultStompFrame(StompMessageType.ACK.stompCommand).apply {
                    headers()
                        .set(StompHeaders.ID, id)
                }
            }
        }
    }

    fun toJson(): String {
        return JSON.toJSONString(this)
    }
}
