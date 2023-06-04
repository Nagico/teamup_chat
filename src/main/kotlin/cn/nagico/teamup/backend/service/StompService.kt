package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.stomp.StompFrame

interface StompService {
    /**
     * 处理CONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onConnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame)

    /**
     * 处理DISCONNECT命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onDisconnect(ctx: ChannelHandlerContext, inboundFrame: StompFrame)

    /**
     * 处理SEND命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onSend(ctx: ChannelHandlerContext, inboundFrame: StompFrame)

    /**
     * 处理SUBSCRIBE命令
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onSubscribe(ctx: ChannelHandlerContext, inboundFrame: StompFrame)

    /**
     * 传递消息 将消息发送给本服务端用户
     *
     *
     * @param stompMessage 消息内容
     */
    fun deliverMessage(stompMessage: StompMessage)

    /**
     * 发送 Error 帧
     *
     * @param message 错误消息
     * @param description 错误详情
     * @param ctx Context
     */
    fun sendErrorFrame(message: String, description: String?, ctx: ChannelHandlerContext)

    /**
     * 处理ack
     *
     * @param ctx Context
     * @param inboundFrame 请求帧
     */
    fun onAck(ctx: ChannelHandlerContext, inboundFrame: StompFrame)
}