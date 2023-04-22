package cn.nagico.teamup.backend.handler



import cn.nagico.teamup.backend.exception.StompCommandError
import cn.nagico.teamup.backend.exception.StompException
import cn.nagico.teamup.backend.exception.StompExceptionType
import cn.nagico.teamup.backend.service.StompService
import cn.nagico.teamup.backend.util.annotation.Slf4j
import cn.nagico.teamup.backend.util.annotation.Slf4j.Companion.logger
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.stomp.StompCommand
import io.netty.handler.codec.stomp.StompFrame
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.net.SocketException

/**
 * 处理stomp协议
 *
 * # 术语
 * - client：客户端，即发送请求的一方
 * - server：服务端，即接收请求的一方
 * - destination：目的地址，clients集合，消息传递的单位
 * - message：消息，即发送的内容
 * - subscription：订阅，即client订阅某个destination，可根据<client, subscription>确定唯一的订阅
 * - transaction：事务，用于批量发送消息
 * - ack：消息确认机制，用于保证消息的可靠传递
 * - id：消息id，用于标识消息；subscription的id，用于标识订阅
 * - receipt：回执，用于确认消息是否发送成功
 * - heart-beat：心跳，用于保持连接
 * - content-length：消息长度，用于标识消息长度
 * - content-type：消息内容类型，用于标识消息内容类型
 * - version：协议版本，用于标识协议版本
 *
 * # 过程：
 * ## **SEND** (client) 向destination发送消息
 * - destination：目的地址，生产者发送消息的目的地址，消费者订阅的地址
 * - content-type：消息内容类型, 默认为text/plain，建议使用application/json
 * - BODY：消息内容，使用UTF-8编码
 * - transaction：事务名
 *
 * ## **SUBSCRIBE** (client) 订阅地址，消费者监听/接受消息
 * - id：client中订阅的唯一标识（对于同一client，可以订阅多个地址，但是id不能重复）。server可以根据<client, id>确定唯一的订阅，便于处理MESSAGE与UNSUBSCRIBE
 * - destination：目的地址，生产者发送消息的目的地址，消费者订阅的地址
 * - ack：消息确认机制，
 *     - auto 表示自动确认，client接收到消息后，server会自动发送ACK，client不需要发送ACK。这种确认模式可能会导致发送到客户端的消息被丢弃。
 *     - client 表示客户端需要发送ACK，server接收到ACK后，会删除消息（此处的删除意思是将该消息标记已确认接收）。如果client放弃处理消息，需要发送NACK。client为累计确认，即client发送ACK后，server会删除所有小于等于该ACK的消息。
 *     - client-individual client的非累计确认，即client发送ACK后，server只会删除该ACK对应的消息。
 *
 * ## **UNSUBSCRIBE** (client) 取消订阅
 * - id：client中订阅的唯一标识，server可以根据<client, id>确定唯一的订阅
 *
 * ## **ACK** (client) 确认消息 **NACK** (client) 拒绝消息
 * - id：message的ack值
 * - transaction + 事务名：事务，表示该(N)ACK属于哪个事务
 *
 * ## **BEGIN**  (client) 开启事务 用于批量发送消息
 * - transaction：事务名，事务标识符将用于SEND、COMMIT、ABORT、ACK和NACK帧，以将它们绑定到对应事务。
 *
 * **COMMIT** (client) 提交事务
 * - transaction：事务名，表示事务结束
 *
 * **ABORT** (client) 回滚事务
 * - transaction：事务名，表示事务终止
 *
 * ## **CONNECT/STOMP** (client) 尝试连接server
 * - accept-version：支持的协议版本，多个版本用逗号分隔
 * - host：vhost地址，暂未使用
 * - login：用户名
 * - passcode：密码
 * - heart-beat：心跳机制，格式为：[outgoing, incoming]，单位为毫秒，例如：[10000,10000]，表示每10秒发送一次心跳，每10秒接收一次心跳
 * - Authentication：认证信息，格式为：scheme token，例如：Bearer xxxx
 *
 * ## **CONNECTED** (server) 响应client连接请求
 * - version：协商后使用的协议版本
 * - heart-beat：协商后的心跳机制
 * - session：会话id，用于标识连接
 * - server：server信息
 * - userId：用户id
 *
 * ## **DISCONNECT** (client) 断开连接
 * - receipt：回执，表示server需要发送RECEPT帧，确保在client断开连接前，已经处理完server所有发送的消息
 *
 * ## **RECEIPT** (server) 确认client断开连接
 * - receipt-id：回执id，表示该RECEIPT对应的帧的id
 *
 * ## **MESSAGE** (server) 向destination转发消息
 * - destination：目的地址。一般由SEND帧指定
 * - message-id：消息id
 * - subscription：订阅id，表示该消息是由哪个订阅转发的
 * - ack：当该订阅采用主动确认时，server会发送该header，内容为随机值，client需要将其填入ACK/NACK帧的id中
 * - content-type：消息内容类型, 默认为text/plain，建议使用application/json
 * - content-length：消息长度，用于标识消息长度
 * - BODY：消息内容，使用UTF-8编码
 *
 * ## **ERROR** (server) 错误响应
 * - message：错误简要信息
 * - BODY：错误详细信息
 *
 *
 */

@Slf4j
@Sharable
@Service
class StompChatHandler : SimpleChannelInboundHandler<StompFrame>() {
    @Autowired
    private lateinit var stompService: StompService

    override fun channelRead0(ctx: ChannelHandlerContext, inboundFrame: StompFrame) {
        val decoderResult = inboundFrame.decoderResult()
        if (decoderResult.isFailure) {  // 失败处理
            stompService.sendErrorFrame("rejected frame", decoderResult.toString(), ctx)
            return
        }
        when (inboundFrame.command()) {  // 根据不同的命令进行处理
            StompCommand.STOMP, StompCommand.CONNECT -> stompService.onConnect(ctx, inboundFrame)  // 连接
            StompCommand.SEND -> stompService.onSend(ctx, inboundFrame)  // 发送
            StompCommand.DISCONNECT -> stompService.onDisconnect(ctx, inboundFrame)  // 断开连接
            StompCommand.ACK -> stompService.onAck(ctx, inboundFrame)  // 确认消息
            else -> throw StompCommandError("Received unsupported command ${inboundFrame.command()}")
        }
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        when (cause) {
            is StompException -> stompService.sendErrorFrame(cause.type.content, cause.message, ctx)
            is SocketException -> {}
            else -> {
                logger.error("Unknown Error", cause)
                stompService.sendErrorFrame(StompExceptionType.UNKNOWN_ERROR.content, cause.message, ctx)
            }
        }
    }

}