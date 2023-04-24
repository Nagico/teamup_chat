package cn.nagico.teamup.backend.server


import cn.nagico.teamup.backend.enums.StompVersion
import cn.nagico.teamup.backend.util.StompWebSocketProtocolCodec
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

    override fun initChannel(channel: SocketChannel) {
        channel.pipeline()
            .addLast(HttpServerCodec())
            .addLast(HttpObjectAggregator(65536))
            .addLast(WebSocketServerProtocolHandler(chatPath, StompVersion.SUB_PROTOCOLS))
            .addLast(stompWebSocketProtocolCodec)
    }
}