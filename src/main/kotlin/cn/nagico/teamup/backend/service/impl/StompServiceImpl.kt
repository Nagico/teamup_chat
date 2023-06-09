package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.jwt.Jwt
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.stomp.entity.StompSubscription
import cn.nagico.teamup.backend.stomp.constant.StompVersion
import cn.nagico.teamup.backend.stomp.exception.StompVersionError
import cn.nagico.teamup.backend.stomp.exception.frame.StompHeadMissing
import cn.nagico.teamup.backend.service.StompMessageService
import cn.nagico.teamup.backend.service.StompService
import cn.nagico.teamup.backend.service.UserService
import cn.nagico.teamup.backend.stomp.exception.StompException
import cn.nagico.teamup.backend.stomp.exception.StompPermissionError
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import io.netty.util.AsciiString
import io.netty.util.AttributeKey
import io.netty.util.CharsetUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import kotlin.collections.HashMap

@Service
class StompServiceImpl: StompService {
    /**
     * 存储用户对应的订阅
     */
    private val destinations = HashMap<Long, StompSubscription>()

    @Autowired
    private lateinit var stompMessageService: StompMessageService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var serverUUID: String

    @Autowired
    private lateinit var jwt: Jwt

    private fun getHeader(frame: StompFrame, header: String): String {
        return frame.headers().getAsString(header) ?: throw StompHeadMissing(header)
    }

    private fun getHeader(frame: StompFrame, header: AsciiString): String {
        return frame.headers().getAsString(header) ?: throw StompHeadMissing(header)
    }

    /**
     * 处理CONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    override fun onConnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        // 获取client支持的stomp版本
        val acceptVersions = getHeader(inboundFrame, StompHeaders.ACCEPT_VERSION)
        // 获取server匹配的stomp版本
        val handshakeAcceptVersion = ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get().also {
            if (!acceptVersions.contains(it.version))
                throw StompVersionError(it.version)
        }

        // DEBUG user直接认证
        val user = try {
             getHeader(inboundFrame, DEBUG_USER).toLong()
        }
        catch (_:StompHeadMissing) {
            // jwt 认证
            val token = getHeader(inboundFrame, AUTH).split(" ")[1]
            val payload = jwt.validateToken(token)
            payload.userId
        }

        userService.online(user)


        ctx.channel().attr(USER).set(user)
        // 发送连接成功帧
        val connectedFrame = DefaultStompFrame(StompCommand.CONNECTED)
        connectedFrame.headers()
            .set(StompHeaders.VERSION, handshakeAcceptVersion.version)
            .set(StompHeaders.SESSION, ctx.channel().id().asLongText())
            .set(StompHeaders.SERVER, "Netty/4.1 ($serverUUID)")
            .set(StompHeaders.HEART_BEAT, "0,0")
            .set("user", user.toString())
        ctx.writeAndFlush(connectedFrame)
    }
    override fun onSubscribe(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val user = ctx.channel().attr(USER).get()!!

        // 检查destination
        val destination = getHeader(inboundFrame, StompHeaders.DESTINATION).toLong()
        if (destination != user) {
            throw StompPermissionError("user $user cannot subscribe to $destination")
        }

        subscribe(user, ctx)

        // 发送未读消息
        for (message in stompMessageService.fetchUnreceivedMessages(user)) {
            ctx.writeAndFlush(message.toStompFrame())
        }
    }

    /**
     * 处理DISCONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    override fun onDisconnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val user = ctx.channel().attr(USER).get()!!
        // 获取receipt
        val receiptId = inboundFrame.headers().getAsString(StompHeaders.RECEIPT) ?: run {
            // 没有回执帧发送请求，直接关闭连接
            ctx.close()
            userService.offline(user)
            return
        }

        // 发送回执帧
        val receiptFrame = DefaultStompFrame(StompCommand.RECEIPT)
        receiptFrame.headers()[StompHeaders.RECEIPT_ID] = receiptId
        ctx.writeAndFlush(receiptFrame).addListener(ChannelFutureListener.CLOSE)  // 发送回执帧后关闭连接
        userService.offline(user)
    }

    /**
     * 处理SEND命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    override fun onSend(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        //获取目的地址
        val destination = getHeader(inboundFrame, StompHeaders.DESTINATION)

        val user = ctx.channel().attr(USER).get()!!
        val stompMessage = StompMessage(inboundFrame, user, destination.toLong())

        stompMessageService.deliverMessage(stompMessage)

        receiptIfNeed(ctx, inboundFrame)
    }

    /**
     * 订阅
     *
     * @param user 用户ID
     * @param ctx Context
     */
    private fun subscribe(
        user: Long,
        ctx: ChannelHandlerContext,
    ) {
        val subscription = StompSubscription(user, ctx.channel())  // 创建订阅对象

        destinations[user] = subscription  // 添加订阅

        ctx.channel()
            .closeFuture()  // 监听channel关闭事件
            .addListener(ChannelFutureListener {
                destinations.remove(user)  // 移除订阅
            })
    }

    /**
     * 传递消息 将消息发送给本服务端用户
     *
     *
     * @param stompMessage 消息内容
     */
    override fun deliverMessage(
        stompMessage: StompMessage,
    ) {
        //获取接收方订阅
        val subscription = destinations[stompMessage.receiver] ?: run {
            return
        }

        subscription.channel.writeAndFlush(stompMessage.toStompFrame())
    }

    override fun sendErrorFrame(message: String, description: String?, ctx: ChannelHandlerContext) {
        val errorFrame = DefaultStompFrame(StompCommand.ERROR)
        errorFrame.headers()[StompHeaders.MESSAGE] = message
        if (description != null) {
            errorFrame.content().writeCharSequence(description, CharsetUtil.UTF_8)
        }
        ctx.writeAndFlush(errorFrame).addListener(ChannelFutureListener.CLOSE)
    }

    /**
     * 发送回执帧
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    private fun receiptIfNeed(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val receiptId = inboundFrame.headers().getAsString(StompHeaders.RECEIPT)  // 获取回执id

        if (receiptId != null) {
            val receiptFrame: StompFrame = DefaultStompFrame(StompCommand.RECEIPT)
            receiptFrame.headers().set(StompHeaders.RECEIPT_ID, receiptId)
            ctx.writeAndFlush(receiptFrame)
        }
    }

    /**
     * 处理ack
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    override fun onAck(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val user = ctx.channel().attr(USER).get()!!
        val messageId = getHeader(inboundFrame, StompHeaders.MESSAGE_ID)
        stompMessageService.ackMessage(user, messageId)
    }

    companion object {
        private val USER = AttributeKey.valueOf<Long>("user")

        private const val AUTH = "Authentication"
        private const val DEBUG_USER = "UserId"
    }
}