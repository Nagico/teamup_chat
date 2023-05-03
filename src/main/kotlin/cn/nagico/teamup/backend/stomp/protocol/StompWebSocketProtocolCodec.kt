package cn.nagico.teamup.backend.stomp.protocol


import cn.nagico.teamup.backend.stomp.constant.StompVersion
import cn.nagico.teamup.backend.server.handler.StompChatHandler
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageCodec
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrame
import io.netty.handler.codec.http.websocketx.WebSocketFrameAggregator
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler.HandshakeComplete
import io.netty.handler.codec.stomp.StompSubframe
import io.netty.handler.codec.stomp.StompSubframeAggregator
import io.netty.handler.codec.stomp.StompSubframeDecoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Sharable
@Service
class StompWebSocketProtocolCodec : MessageToMessageCodec<WebSocketFrame, StompSubframe>() {
    @Autowired
    private lateinit var stompChatHandler: StompChatHandler
    @Autowired
    private lateinit var stompWebSocketFrameEncoder: StompWebSocketFrameEncoder

    /**
     * 当fireUserEventTriggered()方法触发时候被调用
     *
     * 重写userEventTriggered方法，当握手成功后，将StompVersion添加到Channel中
     * 然后将Stomp相关的Handler添加到ChannelPipeline中
     *
     * @param ctx Context
     * @param evt Event
     */
    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any) {
        // 当事件属于HandshakeComplete（握手成功时），与new WebSocketServerProtocolHandler.HandshakeComplete对应
        if (evt is HandshakeComplete) {
            val stompVersion = StompVersion.findBySubProtocol(evt.selectedSubprotocol() ?: "v12.stomp")
            ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).set(stompVersion)
            ctx.pipeline()
                .addLast(WebSocketFrameAggregator(65536))
                .addLast(StompSubframeDecoder())
                .addLast(StompSubframeAggregator(65536))
                .addLast(stompChatHandler)
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }

    /**
     * 将StompSubframe编码为WebSocketFrame
     *
     * @param ctx Context
     * @param stompFrame StompSubframe
     * @param out Output
     */
    override fun encode(ctx: ChannelHandlerContext, stompFrame: StompSubframe?, out: List<Any>) {
        stompWebSocketFrameEncoder.encode(ctx, stompFrame!!, out)
    }

    /**
     * 将WebSocketFrame解码为StompSubframe
     *
     * @param ctx Context
     * @param webSocketFrame WebSocketFrame
     * @param out Output
     */
    override fun decode(ctx: ChannelHandlerContext, webSocketFrame: WebSocketFrame, out: MutableList<Any>) {
        if (webSocketFrame is TextWebSocketFrame || webSocketFrame is BinaryWebSocketFrame) {
            out.add(webSocketFrame.content().retain())
        } else {
            ctx.close()
        }
    }
}