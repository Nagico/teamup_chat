package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.jwt.Jwt
import cn.nagico.teamup.backend.jwt.constant.TokenType
import cn.nagico.teamup.backend.jwt.entity.JwtPayload
import cn.nagico.teamup.backend.service.StompMessageService
import cn.nagico.teamup.backend.service.UserService
import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import cn.nagico.teamup.backend.stomp.constant.StompMessageType
import cn.nagico.teamup.backend.stomp.constant.StompVersion
import cn.nagico.teamup.backend.stomp.entity.StompSubscription
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.message.StompMessageContent
import cn.nagico.teamup.backend.stomp.exception.StompPermissionError
import cn.nagico.teamup.backend.stomp.exception.StompVersionError
import cn.nagico.teamup.backend.stomp.exception.frame.StompHeadMissing
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelId
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import io.netty.util.Attribute
import io.netty.util.AttributeKey
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.*
import org.powermock.api.mockito.PowerMockito
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean

@SpringBootTest
class StompServiceImplTest {
    @Mock
    private lateinit var ctx: ChannelHandlerContext

    @Mock
    private lateinit var stompMessageService: StompMessageService

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var jwt: Jwt

    @Mock
    private lateinit var channel: Channel

    @InjectMocks
    private lateinit var stompService: StompServiceImpl

    @BeforeEach
    fun setup() {
        MockitoAnnotations.openMocks(this)

        Mockito.`when`(ctx.channel()).thenReturn(channel)

        // set stompService.serverUUID
        PowerMockito.field(StompServiceImpl::class.java, "serverUUID").set(stompService, "1")

        val channelId = Mockito.mock(ChannelId::class.java)
        Mockito.`when`(channel.id()).thenReturn(channelId)
        Mockito.`when`(channelId.asLongText()).thenReturn("1")

        // set ctx.channel().attr(StompVersion.USER).get()
        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)
        Mockito.`when`(user.get()).thenReturn(1L)

        val payload = JwtPayload(TokenType.ACCESS, 1L)
        Mockito.`when`(jwt.validateToken("token")).thenReturn(payload)
    }

    @Test
    fun onConnect_debugUser() {
        // set ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get()
        val version = Mockito.mock(Attribute::class.java) as Attribute<StompVersion>
        Mockito.`when`(channel.attr(StompVersion.CHANNEL_ATTRIBUTE_KEY)).thenReturn(version)
        Mockito.`when`(version.get()).thenReturn(StompVersion.STOMP_V12)

        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)

        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.ACCEPT_VERSION, StompVersion.STOMP_V12.version)
        inboundFrame.headers().add("UserId", "1")
        inboundFrame.headers().add(StompHeaders.ID, "1")
        inboundFrame.headers().add(StompHeaders.HEART_BEAT, "0,0")

        stompService.onConnect(ctx, inboundFrame)

        Mockito.verify(userService).online(1)

        val outboundFrame = getOutboundFrame()

        assertEquals(StompCommand.CONNECTED, outboundFrame.command())
        assertEquals("1", outboundFrame.headers().getAsString(StompHeaders.SESSION))
        assertEquals("1.2", outboundFrame.headers().getAsString(StompHeaders.VERSION))
        assertEquals("0,0", outboundFrame.headers().getAsString(StompHeaders.HEART_BEAT))
        assertEquals("1", outboundFrame.headers().getAsString("user"))
        assertEquals("Netty/4.1 (1)", outboundFrame.headers().getAsString("server"))
    }

    @Test
    fun onConnect_jwtToken() {
        // set ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get()
        val version = Mockito.mock(Attribute::class.java) as Attribute<StompVersion>
        Mockito.`when`(channel.attr(StompVersion.CHANNEL_ATTRIBUTE_KEY)).thenReturn(version)
        Mockito.`when`(version.get()).thenReturn(StompVersion.STOMP_V12)

        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)

        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.ACCEPT_VERSION, StompVersion.STOMP_V12.version)
        inboundFrame.headers().add("Authentication", "Bearer token")
        inboundFrame.headers().add(StompHeaders.ID, "1")
        inboundFrame.headers().add(StompHeaders.HEART_BEAT, "0,0")

        stompService.onConnect(ctx, inboundFrame)

        Mockito.verify(userService).online(1)

        val outboundFrame = getOutboundFrame()

        assertEquals(StompCommand.CONNECTED, outboundFrame.command())
        assertEquals("1", outboundFrame.headers().getAsString(StompHeaders.SESSION))
        assertEquals("1.2", outboundFrame.headers().getAsString(StompHeaders.VERSION))
        assertEquals("0,0", outboundFrame.headers().getAsString(StompHeaders.HEART_BEAT))
        assertEquals("1", outboundFrame.headers().getAsString("user"))
        assertEquals("Netty/4.1 (1)", outboundFrame.headers().getAsString("server"))
    }

    @Test
    fun onConnect_unsupportedVersion() {
        // set ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get()
        val version = Mockito.mock(Attribute::class.java) as Attribute<StompVersion>
        Mockito.`when`(channel.attr(StompVersion.CHANNEL_ATTRIBUTE_KEY)).thenReturn(version)
        Mockito.`when`(version.get()).thenReturn(StompVersion.STOMP_V12)

        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)

        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.ACCEPT_VERSION, StompVersion.STOMP_V11.version)
        inboundFrame.headers().add("Authentication", "Bearer token")
        inboundFrame.headers().add(StompHeaders.ID, "1")
        inboundFrame.headers().add(StompHeaders.HEART_BEAT, "0,0")

        assertThrows(StompVersionError::class.java) {
            stompService.onConnect(ctx, inboundFrame)
        }
    }

    @Test
    fun onConnect_anonymous() {
        // set ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get()
        val version = Mockito.mock(Attribute::class.java) as Attribute<StompVersion>
        Mockito.`when`(channel.attr(StompVersion.CHANNEL_ATTRIBUTE_KEY)).thenReturn(version)
        Mockito.`when`(version.get()).thenReturn(StompVersion.STOMP_V12)

        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)

        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.ACCEPT_VERSION, StompVersion.STOMP_V12.version)
        inboundFrame.headers().add(StompHeaders.ID, "1")
        inboundFrame.headers().add(StompHeaders.HEART_BEAT, "0,0")

        assertThrows(StompHeadMissing::class.java) {
            stompService.onConnect(ctx, inboundFrame)
        }
    }

    @Test
    fun onSubscribe() {
        val message = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 2L,
            receiver = 1L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        val closeFuture = Mockito.mock(ChannelFuture::class.java)
        Mockito.`when`(channel.closeFuture()).thenReturn(closeFuture)

        Mockito.`when`(stompMessageService.fetchUnreceivedMessages(1)).thenReturn(listOf(message))

        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.DESTINATION, "1")

        stompService.onSubscribe(ctx, inboundFrame)

        Mockito.verify(closeFuture).addListener(Mockito.any())

        val outboundFrames = getOutboundFrames(1)
        val outboundFrame = outboundFrames[0]

        assertEquals(message.toStompFrame().content(), outboundFrames[0].content())
        assertEquals(StompCommand.MESSAGE, outboundFrame.command())
        assertEquals(message.toStompFrame().headers(), outboundFrames[0].headers())

    }

    @Test
    fun onSubscribe_invalidDestination() {
        val inboundFrame = DefaultStompFrame(StompCommand.CONNECT)
        inboundFrame.headers().add(StompHeaders.DESTINATION, "2")

        assertThrows(StompPermissionError::class.java) {
            stompService.onSubscribe(ctx, inboundFrame)
        }
    }

    @Test
    fun onDisconnect_noReceipt() {
        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)
        Mockito.`when`(user.get()).thenReturn(1L)

        val inboundFrame = DefaultStompFrame(StompCommand.DISCONNECT)

        stompService.onDisconnect(ctx, inboundFrame)

        Mockito.verify(userService).offline(1)
        Mockito.verify(ctx).close()
    }

    @Test
    fun onDisconnect_withReceipt() {
        val user = Mockito.mock(Attribute::class.java) as Attribute<Long>
        Mockito.`when`(channel.attr(AttributeKey.valueOf<Long>("user"))).thenReturn(user)
        Mockito.`when`(user.get()).thenReturn(1L)

        Mockito.`when`(ctx.writeAndFlush(Mockito.any())).thenReturn(Mockito.mock(ChannelFuture::class.java))

        val inboundFrame = DefaultStompFrame(StompCommand.DISCONNECT)
        inboundFrame.headers().add(StompHeaders.RECEIPT, "1")

        stompService.onDisconnect(ctx, inboundFrame)

        Mockito.verify(userService).offline(1)

        val outboundFrame = getOutboundFrame()

        assertEquals(StompCommand.RECEIPT, outboundFrame.command())
        assertEquals("1", outboundFrame.headers().getAsString(StompHeaders.RECEIPT_ID))
    }
    @Test
    fun onSend() {
        val content = "{\"type\":1,\"content\":\"test\"}"

        val inboundFrame = DefaultStompFrame(StompCommand.SEND)
        inboundFrame.headers().add(StompHeaders.DESTINATION, "2")
        inboundFrame.headers().add(StompHeaders.CONTENT_TYPE, "application/json")
        inboundFrame.headers().add(StompHeaders.CONTENT_LENGTH, content.toByteArray().size.toString())
        inboundFrame.headers().add(StompHeaders.RECEIPT, "1")
        inboundFrame.headers().add(StompHeaders.ID, "1")
        inboundFrame.content().writeBytes(content.toByteArray())

        stompService.onSend(ctx, inboundFrame)
        val argumentCaptor = com.nhaarman.mockitokotlin2.argumentCaptor<StompMessage>()
        Mockito.verify(stompMessageService).deliverMessage(argumentCaptor.capture())
        val stompMessage = argumentCaptor.firstValue

        assertEquals("1", stompMessage.id)
        assertEquals(StompMessageType.MESSAGE, stompMessage.type)
        assertEquals(1L, stompMessage.sender)
        assertEquals(2L, stompMessage.receiver)
        assertEquals("test", stompMessage.content.content)
        assertEquals(StompMessageContentType.CHAT, stompMessage.content.type)


        val outboundFrame = getOutboundFrame()

        assertEquals(StompCommand.RECEIPT, outboundFrame.command())
        assertEquals("1", outboundFrame.headers().getAsString(StompHeaders.RECEIPT_ID))
    }

    @Test
    fun deliverMessage_online() {
        // add user to destinations
        val destinationsField = stompService.javaClass.getDeclaredField("destinations")
        destinationsField.isAccessible = true
        val destinations = destinationsField.get(stompService) as HashMap<Long, StompSubscription>
        destinations[2] = StompSubscription(1, channel)

        val message = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 1L,
            receiver = 2L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        stompService.deliverMessage(message)
        val argumentCaptor = ArgumentCaptor.forClass(DefaultStompFrame::class.java)
        Mockito.verify(channel).writeAndFlush(argumentCaptor.capture())
        val outboundFrame = argumentCaptor.value

        assertEquals(message.toStompFrame().content(), outboundFrame.content())
        assertEquals(StompCommand.MESSAGE, outboundFrame.command())
        assertEquals(message.toStompFrame().headers(), outboundFrame.headers())
    }

    @Test
    fun deliverMessage_offline() {
        val message = StompMessage(
            id = "messageId",
            type = StompMessageType.MESSAGE,
            sender = 1L,
            receiver = 2L,
            content = StompMessageContent(StompMessageContentType.CHAT, "content"),
            createTime = 1L
        )

        stompService.deliverMessage(message)
    }

    @Test
    fun sendErrorFrame() {
        val closeFuture = Mockito.mock(ChannelFuture::class.java)
        Mockito.`when`(ctx.writeAndFlush(Mockito.any())).thenReturn(closeFuture)

        stompService.sendErrorFrame("message", "description", ctx)

        Mockito.verify(closeFuture).addListener(Mockito.any())
        val outboundFrame = getOutboundFrame()

        assertEquals(StompCommand.ERROR, outboundFrame.command())
        assertEquals("message", outboundFrame.headers().getAsString(StompHeaders.MESSAGE))
    }

    @Test
    fun onAck() {
        val inboundFrame = DefaultStompFrame(StompCommand.ACK)
        inboundFrame.headers().add(StompHeaders.MESSAGE_ID, "1")

        stompService.onAck(ctx, inboundFrame)

        Mockito.verify(stompMessageService).ackMessage(1L, "1")
    }


    private fun getOutboundFrame(): DefaultStompFrame {
        val argumentCaptor = ArgumentCaptor.forClass(DefaultStompFrame::class.java)
        Mockito.verify(ctx).writeAndFlush(argumentCaptor.capture())
        return argumentCaptor.value
    }

    private fun getOutboundFrames(times: Int): List<DefaultStompFrame> {
        val argumentCaptor = ArgumentCaptor.forClass(DefaultStompFrame::class.java)
        Mockito.verify(ctx, Mockito.times(times)).writeAndFlush(argumentCaptor.capture())
        return argumentCaptor.allValues
    }
}