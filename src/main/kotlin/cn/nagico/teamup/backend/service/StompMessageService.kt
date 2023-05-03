package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.util.UUIDUtil
import java.util.*

interface StompMessageService {
    /**
     * 获取消息
     *
     * @param messageId 消息id
     * @return stomp消息
     */
    fun getMessage(messageId: UUID): StompMessage {
        return getMessage(UUIDUtil.toHex(messageId))
    }

    /**
     * 获取消息
     *
     * @param messageId 消息id (hex格式，无 dashes)
     * @return stomp消息
     */
    fun getMessage(messageId: String): StompMessage

    /**
     * 消息投递
     *
     * @param message stomp消息
     */
    fun deliverMessage(message: StompMessage)

    /**
     * 确认消息送达
     *
     * @param userId 用户id
     * @param messageId 消息id
     */
    fun ackMessage(userId: Long, messageId: String)

    /**
     * 获取用户未接收消息
     *
     * @param userId
     * @return
     */
    fun fetchUnreceivedMessages(userId: Long): List<StompMessage>

}