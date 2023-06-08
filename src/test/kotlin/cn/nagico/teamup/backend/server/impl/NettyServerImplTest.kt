package cn.nagico.teamup.backend.server.impl

import cn.nagico.teamup.backend.util.properties.NettyProperties
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.nio.NioEventLoopGroup
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class NettyServerImplTest {
    @Mock
    private lateinit var bossGroup: NioEventLoopGroup

    @Mock
    private lateinit var workerGroup: NioEventLoopGroup

    @Mock
    private lateinit var serverBootstrap: ServerBootstrap

    @Mock
    private lateinit var nettyProperties: NettyProperties

    @Mock
    private lateinit var channelFuture: ChannelFuture

    @InjectMocks
    private lateinit var nettyServer: NettyServerImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testStart() {
        Mockito.`when`(nettyProperties.port).thenReturn(8080)
        Mockito.`when`(serverBootstrap.bind(8080)).thenReturn(channelFuture)
        Mockito.`when`(channelFuture.sync()).thenReturn(channelFuture)

        nettyServer.start()

        Mockito.verify(serverBootstrap).bind(8080)
        Mockito.verify(channelFuture).sync()
    }

    @Test
    fun testClose() {
        nettyServer.close()

        Mockito.verify(bossGroup).shutdownGracefully()
        Mockito.verify(workerGroup).shutdownGracefully()
    }
}