package cn.nagico.teamup.backend.chat.service

import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.chat.entity.StompData
import cn.nagico.teamup.backend.chat.entity.StompSubscription
import cn.nagico.teamup.backend.chat.enums.StompVersion
import cn.nagico.teamup.backend.constant.status.UserStatus
import cn.nagico.teamup.backend.entity.User
import cn.nagico.teamup.backend.util.jwt.exception.JwtException
import cn.nagico.teamup.utils.jwt.JwtUtils
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.stomp.DefaultStompFrame
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import io.netty.handler.codec.stomp.StompHeaders
import io.netty.util.AttributeKey
import io.netty.util.CharsetUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.HashMap

@Service
class StompService {
    /**
     * 存储用户对应的订阅
     */
    private val destinations = HashMap<Long, StompSubscription>()

    @Autowired
    private lateinit var userCacheManager: UserCacheManager

    /**
     * 处理CONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onConnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        // 获取client支持的stomp版本
        val acceptVersions = inboundFrame.headers().getAsString(StompHeaders.ACCEPT_VERSION)
        // 获取server匹配的stomp版本
        val handshakeAcceptVersion: StompVersion = ctx.channel().attr(StompVersion.CHANNEL_ATTRIBUTE_KEY).get()
        // 版本不匹配
        if (acceptVersions == null || !acceptVersions.contains(handshakeAcceptVersion.version)) {
            sendErrorFrame(
                "invalid version",
                "Received invalid version, expected " + handshakeAcceptVersion.version, ctx
            )
            return
        }

        // 获取认证header
        val authentication = inboundFrame.headers().getAsString("Authentication") ?: run {
            sendErrorFrame("missed header", "Required 'Authentication' header missed", ctx)
            return
        }

        val user: User

        try {
            val token = authentication.split(" ")[1]
            val payload = JwtUtils.validateToken(token)
            user = User(payload.userId, userCacheManager)
        } catch (e: JwtException) {
            sendErrorFrame("invalid token", "Received invalid token", ctx)
            return
        }


        try {
            user.status = UserStatus.Online
        } catch (e: Exception) {
//            sendErrorFrame("invalid token", "Received invalid token", ctx)
//            return
            userCacheManager.setUserStatusCache(user.id, UserStatus.Online)
        }


        ctx.channel().attr(USER).set(user)
        subscribe(user, ctx)

        // 发送连接成功帧
        val connectedFrame = DefaultStompFrame(StompCommand.CONNECTED)
        connectedFrame.headers()
            .set(StompHeaders.VERSION, handshakeAcceptVersion.version)
            .set(StompHeaders.SESSION, ctx.channel().id().asLongText())
            .set(StompHeaders.SERVER, "Netty/4.1")
            .set(StompHeaders.HEART_BEAT, "0,0")
            .set("user", user.id.toString())
        ctx.writeAndFlush(connectedFrame)
    }

    /**
     * 处理DISCONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onDisconnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val user = ctx.channel().attr(USER).get()!!
        // 获取receipt
        val receiptId = inboundFrame.headers().getAsString(StompHeaders.RECEIPT) ?: run {
            // 没有回执帧发送请求，直接关闭连接
            ctx.close()
            user.status = UserStatus.Offline
            return
        }

        // 发送回执帧
        val receiptFrame = DefaultStompFrame(StompCommand.RECEIPT)
        receiptFrame.headers()[StompHeaders.RECEIPT_ID] = receiptId
        ctx.writeAndFlush(receiptFrame).addListener(ChannelFutureListener.CLOSE)  // 发送回执帧后关闭连接
        user.status = UserStatus.Offline
    }

    /**
     * 处理SEND命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onSend(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        //获取目的地址
        val destination = inboundFrame.headers().getAsString(StompHeaders.DESTINATION) ?: run {
            sendErrorFrame("missed header", "Required 'destination' header missed", ctx)
            return
        }

        val user = ctx.channel().attr(USER).get()!!
        val stompData = StompData(inboundFrame, user.id, destination.toLong())

        deliverStompData(stompData)
    }

    /**
     * 订阅
     *
     * @param destination 订阅地址
     * @param subscriptionId 订阅id
     * @param ctx Context
     */
    private fun subscribe(
        user: User,
        ctx: ChannelHandlerContext,
    ) {
        val subscription = StompSubscription(user, ctx.channel())  // 创建订阅对象

        destinations[user.id] = subscription  // 添加订阅

        ctx.channel()
            .closeFuture()  // 监听channel关闭事件
            .addListener(ChannelFutureListener {
                destinations.remove(user.id)  // 移除订阅
            })
    }

    /**
     * 传递消息 将消息发送给指定用户
     *
     *
     * @param stompData 消息内容
     */
    private fun deliverStompData(
        stompData: StompData,
    ) {
        //获取接收方订阅
        val subscription = destinations[stompData.receiver] ?: run {
            // TODO 没有订阅 or 不在线
            return
        }

        subscription.channel.writeAndFlush(stompData.toStompFrame())
    }

    fun sendErrorFrame(message: String, description: String?, ctx: ChannelHandlerContext) {
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
    fun onAck(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val sender: User = ctx.channel().attr(USER).get()!!
        // TODO redis 获取对应user
        val receiver = User(2, userCacheManager)

        val data = StompData(inboundFrame, sender.id, receiver.id)


        deliverStompData(data)
    }

    companion object {
        private val USER = AttributeKey.valueOf<User>("user")
    }
}