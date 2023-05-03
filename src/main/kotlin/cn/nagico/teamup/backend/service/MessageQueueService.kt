package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.stomp.entity.message.StompMessage

interface MessageQueueService {
    /**
     * 保存消息
     *
     * @param stompMessage
     */
    fun saveStompMessage(stompMessage: StompMessage)

    /**
     * 转发消息
     *
     * @param target
     * @param stompMessage
     */
    fun forwardStompMessage(target: String, stompMessage: StompMessage)
}