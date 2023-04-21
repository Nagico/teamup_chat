package cn.nagico.teamup.backend.chat.entity

import cn.nagico.teamup.backend.chat.enums.StompContentType
import cn.nagico.teamup.backend.chat.enums.StompDataType
import cn.nagico.teamup.backend.chat.exception.frame.StompHeadMissing
import io.netty.buffer.Unpooled
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders

data class StompData(
    val id: String,
    val type: StompDataType,
    val sender: Long,
    val receiver: Long,
    val content: String?,
    val createTime: Long,
) {
    constructor(frame: StompFrame, sender: Long, receiver: Long) : this(
        id = frame.headers().getAsString(StompHeaders.ID) ?: throw StompHeadMissing("Required 'id' header missed"),
        type = StompDataType.of(frame.command()),
        sender = sender,
        receiver = receiver,
        content = frame.content().toString(Charsets.UTF_8),
        createTime = System.currentTimeMillis(),
    )

    fun toStompFrame(): StompFrame {
        return when (type) {
            StompDataType.MESSAGE -> {
                DefaultStompFrame(StompDataType.MESSAGE.stompCommand, Unpooled.copiedBuffer(content, Charsets.UTF_8)).apply {
                    headers()
                        .set(StompHeaders.ID, id)
                        .set(StompHeaders.CONTENT_TYPE, StompContentType.JSON.contentType)
                        .set(StompHeaders.CONTENT_LENGTH, (content?.length ?: 0).toString())
                }
            }
            StompDataType.ACK -> {
                DefaultStompFrame(StompDataType.ACK.stompCommand).apply {
                    headers()
                        .set(StompHeaders.ID, id)
                }
            }
        }
    }
}
