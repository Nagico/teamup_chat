package cn.nagico.teamup.backend.chat.entity

import cn.nagico.teamup.backend.chat.enums.StompContentType
import cn.nagico.teamup.backend.chat.enums.StompMessageType
import cn.nagico.teamup.backend.chat.exception.frame.StompHeadMissing
import cn.nagico.teamup.backend.model.Message
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import io.netty.buffer.Unpooled
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import java.util.UUID

data class StompMessage(
    val id: UUID,
    val type: StompMessageType,
    val sender: Long,
    val receiver: Long,
    val content: String?,
    val createTime: Long,
) {
    constructor(frame: StompFrame, sender: Long, receiver: Long) : this(
        id = UUID.fromString(frame.headers().getAsString(StompHeaders.ID) ?: throw StompHeadMissing("Required 'id' header missed")),
        type = StompMessageType.of(frame.command()),
        sender = sender,
        receiver = receiver,
        content = frame.content().toString(Charsets.UTF_8),
        createTime = System.currentTimeMillis(),
    )

    constructor(message: Message) : this(
        id = UUIDUtil.fromHex(message.id!!),
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
                        .set(StompHeaders.ID, id.toString())
                        .set(StompHeaders.CONTENT_TYPE, StompContentType.JSON.contentType)
                        .set(StompHeaders.CONTENT_LENGTH, (content?.length ?: 0).toString())
                }
            }
            StompMessageType.ACK -> {
                DefaultStompFrame(StompMessageType.ACK.stompCommand).apply {
                    headers()
                        .set(StompHeaders.ID, id.toString())
                }
            }
        }
    }
}
