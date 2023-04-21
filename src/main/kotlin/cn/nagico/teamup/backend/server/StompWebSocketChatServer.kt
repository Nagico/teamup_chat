package cn.nagico.teamup.backend.server

import cn.nagico.teamup.backend.util.annotation.Slf4j
import cn.nagico.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.nagico.teamup.backend.util.properties.NettyProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFutureListener
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import jakarta.annotation.Resource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Slf4j
@Component
class StompWebSocketChatServer {
    @Autowired
    private lateinit var bossGroup: NioEventLoopGroup

    @Autowired
    private lateinit var workerGroup: NioEventLoopGroup

    @Autowired
    private lateinit var serverBootstrap: ServerBootstrap

    @Autowired
    private lateinit var nettyProperties: NettyProperties

    /**
     * 启动netty
     *
     * @throws InterruptedException
     */
    @PostConstruct
    fun start() {
        // 绑定端口启动
        serverBootstrap.bind(nettyProperties.port).sync()
        logger.info("Netty Start at port: ${nettyProperties.port}")
    }

    /**
     * 关闭netty
     */
    @PreDestroy
    fun close() {
        logger.info("Netty Close")
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}