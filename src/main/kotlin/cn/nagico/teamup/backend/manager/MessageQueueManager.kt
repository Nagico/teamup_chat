package cn.nagico.teamup.backend.manager

import cn.nagico.teamup.backend.entity.StompMessage
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageQueueManager {
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    fun saveStompMessage(stompMessage: StompMessage) {
        rabbitTemplate.convertAndSend("teamup.direct.save", "", stompMessage.toJson())
    }

    fun deliverStompMessage(target: String, stompMessage: StompMessage) {
        rabbitTemplate.convertAndSend("teamup.direct.deliver", target, stompMessage.toJson())
    }
}