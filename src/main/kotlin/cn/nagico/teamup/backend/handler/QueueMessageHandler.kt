package cn.nagico.teamup.backend.handler

import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.service.StompService
import cn.nagico.teamup.backend.util.config.RabbitMQConfig
import com.alibaba.fastjson.JSON
import org.springframework.amqp.rabbit.annotation.RabbitHandler
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component


@Component
class QueueMessageHandler {
    @Autowired
    private lateinit var rabbitMQConfig: RabbitMQConfig

    @Autowired
    private lateinit var stompService: StompService

    @RabbitListener(queues = ["#{rabbitMQConfig.DELIVER_QUEUE_NAME()}"])
    @RabbitHandler
    fun handleDeliverMessage(@Payload data: String) {
        val message = JSON.parseObject(data, StompMessage::class.java)
        stompService.deliverMessage(message)
    }


}