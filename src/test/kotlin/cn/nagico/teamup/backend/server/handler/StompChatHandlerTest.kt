package cn.nagico.teamup.backend.server.handler

import cn.nagico.teamup.backend.service.StompService
import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompExceptionType
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.DecoderResult
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.springframework.boot.test.context.SpringBootTest
import java.lang.reflect.InvocationTargetException
import java.net.SocketException
import java.text.ParseException

@SpringBootTest
class StompChatHandlerTest {
    @Mock
    private lateinit var ctx: ChannelHandlerContext
    @Mock
    private lateinit var stompService: StompService

    @InjectMocks
    private lateinit var stompChatHandler: StompChatHandler

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsOnConnect() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.CONNECT)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).onConnect(ctx, inboundFrame)
    }

    @Test
    fun testChannelRead0_CommandSend_CallsOnSend() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.SEND)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).onSend(ctx, inboundFrame)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsOnSubscribe() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.SUBSCRIBE)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).onSubscribe(ctx, inboundFrame)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsOnDisconnect() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.DISCONNECT)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).onDisconnect(ctx, inboundFrame)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsOnAck() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.ACK)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).onAck(ctx, inboundFrame)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsErrorDecode() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(true)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.ABORT)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        function.invoke(stompChatHandler, ctx, inboundFrame)

        Mockito.verify(stompService).sendErrorFrame("rejected frame", decoderResult.toString(), ctx)
    }

    @Test
    fun testChannelRead0_CommandStomp_CallsErrorFrame() {
        val inboundFrame = Mockito.mock(StompFrame::class.java)
        val decoderResult = Mockito.mock(DecoderResult::class.java)
        Mockito.`when`(decoderResult.isFailure).thenReturn(false)
        Mockito.`when`(inboundFrame.decoderResult()).thenReturn(decoderResult)
        Mockito.`when`(inboundFrame.command()).thenReturn(StompCommand.ABORT)

        // 调用被测试的方法（反射）
        val function = StompChatHandler::class.java.getDeclaredMethod("channelRead0", ChannelHandlerContext::class.java, StompFrame::class.java)
        function.isAccessible = true
        try {
            function.invoke(stompChatHandler, ctx, inboundFrame)
        } catch (e: InvocationTargetException) {
            assertEquals(e.cause!!.message, "Received unsupported command ABORT")
        }
    }

    @Test
    fun testExceptionCaught_StompException_CallsSendErrorFrame() {
        val cause = Mockito.mock(StompException::class.java)
        Mockito.`when`(cause.type).thenReturn(StompExceptionType.FRAME_ERROR)
        Mockito.`when`(cause.message).thenReturn("Some error message")

        stompChatHandler.exceptionCaught(ctx, cause)

        Mockito.verify(stompService).sendErrorFrame(StompExceptionType.FRAME_ERROR.content, "Some error message", ctx)
    }

    @Test
    fun testExceptionCaught_UnknownError_CallsSendErrorFrameAndLogsError() {
        val cause = Mockito.mock(ParseException::class.java)
        Mockito.`when`(cause.message).thenReturn("Some error message")

        stompChatHandler.exceptionCaught(ctx, cause)

        Mockito.verify(stompService).sendErrorFrame(StompExceptionType.UNKNOWN_ERROR.content, "Some error message", ctx)
    }

    @Test
    fun testExceptionCaught_UnknownError_CallsIgnoreError() {
        val cause = Mockito.mock(SocketException::class.java)
        Mockito.`when`(cause.message).thenReturn("Some error message")

        stompChatHandler.exceptionCaught(ctx, cause)

        Mockito.verify(stompService, times(0)).sendErrorFrame(StompExceptionType.UNKNOWN_ERROR.content, "Some error message", ctx)
    }
}