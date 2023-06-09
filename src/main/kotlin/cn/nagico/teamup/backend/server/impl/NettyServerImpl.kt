package cn.nagico.teamup.backend.server.impl

import cn.nagico.teamup.backend.server.NettyServer
import cn.nagico.teamup.backend.util.annotation.Slf4j
import cn.nagico.teamup.backend.util.annotation.Slf4j.Companion.logger
import cn.nagico.teamup.backend.util.properties.NettyProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.nio.NioEventLoopGroup
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Slf4j
@Component
class NettyServerImpl: NettyServer {
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
    override fun start() {
        // 绑定端口启动
        try {
            serverBootstrap.bind(nettyProperties.port).sync()
            logger.info("Netty Start at port: ${nettyProperties.port}")
        } catch (e: Exception) {
            serverBootstrap.bind(nettyProperties.portSalve).sync()
            logger.info("Netty Start at port: ${nettyProperties.portSalve}")
        }

    }

    /**
     * 关闭netty
     */
    @PreDestroy
    override fun close() {
        logger.info("Netty Close")
        bossGroup.shutdownGracefully()
        workerGroup.shutdownGracefully()
    }
}