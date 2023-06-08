package cn.nagico.teamup.backend.server.handler

import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelFutureListener
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.http.*
import org.junit.jupiter.api.BeforeEach
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations

@SpringBootTest
class CorsHandlerTest {

    @Mock
    private lateinit var ctx: ChannelHandlerContext

    @Mock
    private lateinit var channelFuture: ChannelFuture

    @Autowired
    private lateinit var corsHandler: CorsHandler

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testChannelRead0_OptionsRequest_ResponseSent() {
        // 创建模拟的 FullHttpRequest
        val request = Mockito.mock(FullHttpRequest::class.java)
        val headers = Mockito.mock(HttpHeaders::class.java)

        Mockito.`when`(request.method()).thenReturn(HttpMethod.OPTIONS)
        Mockito.`when`(request.headers()).thenReturn(headers)
        Mockito.`when`(ctx.writeAndFlush(Mockito.any(DefaultFullHttpResponse::class.java))).thenReturn(channelFuture)

        // 调用被测试的方法（反射）
        val function = CorsHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, HttpObject::class.java)
        function.isAccessible = true
        function.invoke(corsHandler, ctx, request)

        // 验证是否正确处理了 OPTIONS 请求并发送了响应
        Mockito.verify(ctx).writeAndFlush(Mockito.any(DefaultFullHttpResponse::class.java))
        Mockito.verify(channelFuture).addListener(ChannelFutureListener.CLOSE)
    }

    @Test
    fun testChannelRead0_NonOptionsRequest_Forwarded() {
        // 创建模拟的 FullHttpRequest
        val request = Mockito.mock(FullHttpRequest::class.java)
        val headers = Mockito.mock(HttpHeaders::class.java)

        Mockito.`when`(request.method()).thenReturn(HttpMethod.GET)
        Mockito.`when`(request.headers()).thenReturn(headers)
        Mockito.`when`(ctx.writeAndFlush(Mockito.any(DefaultFullHttpResponse::class.java))).thenReturn(channelFuture)

        // 调用被测试的方法（反射）
        val function = CorsHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, HttpObject::class.java)
        function.isAccessible = true
        function.invoke(corsHandler, ctx, request)

        // 验证是否正确转发了非 OPTIONS 请求
        Mockito.verify(ctx).fireChannelRead(Mockito.eq(request.retain()))
    }
}
