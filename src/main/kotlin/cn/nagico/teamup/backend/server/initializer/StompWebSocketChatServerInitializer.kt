package cn.nagico.teamup.backend.server.initializer


import cn.nagico.teamup.backend.stomp.constant.StompVersion
import cn.nagico.teamup.backend.server.handler.CorsHandler
import cn.nagico.teamup.backend.stomp.protocol.StompWebSocketProtocolCodec
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service


@Service
class StompWebSocketChatServerInitializer : ChannelInitializer<SocketChannel>() {
    private val chatPath: String = "/"

    @Autowired
    private lateinit var stompWebSocketProtocolCodec: StompWebSocketProtocolCodec

    @Autowired
    private lateinit var corsHandler: CorsHandler

    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
            .addLast(HttpServerCodec())
            .addLast(HttpObjectAggregator(65536))
            .addLast(corsHandler)
            .addLast(WebSocketServerProtocolHandler(chatPath, StompVersion.SUB_PROTOCOLS))
            .addLast(stompWebSocketProtocolCodec)
    }
}