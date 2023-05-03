package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.service.MessageQueueService
import cn.nagico.teamup.backend.util.config.RabbitMQConfig
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageQueueServiceImpl: MessageQueueService {
    @Autowired
    private lateinit var rabbitTemplate: RabbitTemplate

    override fun saveStompMessage(stompMessage: StompMessage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.SAVE_EXCHANGE_NAME, "", stompMessage.toJson())
    }

    override fun forwardStompMessage(target: String, stompMessage: StompMessage) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.DELIVER_EXCHANGE_NAME, target, stompMessage.toJson())
    }
}