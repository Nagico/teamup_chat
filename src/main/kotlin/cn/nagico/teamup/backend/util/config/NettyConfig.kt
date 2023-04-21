package cn.nagico.teamup.backend.util.config

import cn.nagico.teamup.backend.server.StompWebSocketChatServerInitializer
import cn.nagico.teamup.backend.util.properties.NettyProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties
class NettyConfig {
    @Autowired
    private lateinit var nettyProperties: NettyProperties

    @Autowired
    private lateinit var stompWebSocketChatServerInitializer: StompWebSocketChatServerInitializer

    /**
     * boss线程池-进行客户端连接
     *
     * @return
     */
    @Bean
    fun bossGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.boss)
    }

    /**
     * worker线程池-进行业务处理
     *
     * @return
     */
    @Bean
    fun workerGroup(): NioEventLoopGroup {
        return NioEventLoopGroup(nettyProperties.worker)
    }

    /**
     * 服务端启动器，监听客户端连接
     *
     * @return
     */
    @Bean
    fun serverBootstrap(): ServerBootstrap {
        return ServerBootstrap()
            .group(bossGroup(), workerGroup()) // 指定使用的线程组
            .channel(NioServerSocketChannel::class.java) // 指定使用的通道
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, nettyProperties.timeout) // 指定连接超时时间
            .childHandler(stompWebSocketChatServerInitializer)  // 指定worker处理器
    }
}