package cn.nagico.teamup.backend.mq

import org.springframework.messaging.handler.annotation.Payload


interface MessageQueue {

    /**
     * 处理mq消息
     *
     * @param data
     */
    fun handleDeliverMessage(@Payload data: String)
}