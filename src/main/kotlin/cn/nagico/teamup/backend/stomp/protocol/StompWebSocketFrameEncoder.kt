package cn.nagico.teamup.backend.stomp.protocol

import io.netty.buffer.ByteBuf
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.ContinuationWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.handler.codec.stomp.*
import org.springframework.stereotype.Service


@Service
class StompWebSocketFrameEncoder : StompSubframeEncoder() {
    public override fun encode(ctx: ChannelHandlerContext, msg: StompSubframe, out: List<Any>) {
        super.encode(ctx, msg, out)
    }

    override fun convertFullFrame(original: StompFrame, encoded: ByteBuf): WebSocketFrame {
        return if (isTextFrame(original)) {
            TextWebSocketFrame(encoded)
        } else BinaryWebSocketFrame(encoded)
    }

    override fun convertHeadersSubFrame(original: StompHeadersSubframe, encoded: ByteBuf): WebSocketFrame {
        return if (isTextFrame(original)) {
            TextWebSocketFrame(false, 0, encoded)
        } else BinaryWebSocketFrame(false, 0, encoded)
    }

    override fun convertContentSubFrame(original: StompContentSubframe, encoded: ByteBuf): WebSocketFrame {
        return if (original is LastStompContentSubframe) {
            ContinuationWebSocketFrame(true, 0, encoded)
        } else ContinuationWebSocketFrame(false, 0, encoded)
    }

    companion object {
        private fun isTextFrame(headersSubframe: StompHeadersSubframe): Boolean {
            val contentType = headersSubframe.headers().getAsString(StompHeaders.CONTENT_TYPE)
            return contentType != null && (contentType.startsWith("text") || contentType.startsWith("application/json"))
        }
    }
}