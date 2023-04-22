package cn.nagico.teamup.backend.util.config

import cn.nagico.teamup.backend.util.annotation.Slf4j
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Slf4j
@Configuration
class RabbitMQConfig {
    @Autowired
    private lateinit var serverUUID: String

    // Queue
    @Bean
    fun messageSaveQueue(): Queue {
        return Queue(SAVE_QUEUE_NAME, false, false, false)
    }

    @Bean
    fun messageDeliverQueue(): Queue {
        return Queue(DELIVER_QUEUE_NAME(), false, false, true)
    }

    // Exchange
    @Bean
    fun saveExchange(): DirectExchange {
        return DirectExchange(SAVE_EXCHANGE_NAME)
    }

    @Bean
    fun deliverExchange(): DirectExchange {
        return DirectExchange(DELIVER_EXCHANGE_NAME)
    }

    // Binding
    @Bean
    fun bindingSaveExchangeMessageQueue(): Binding {
        return BindingBuilder.bind(messageSaveQueue()).to(saveExchange()).with("")
    }

    @Bean
    fun bindingDeliverExchangeMessageQueue(): Binding {
        return BindingBuilder.bind(messageDeliverQueue()).to(deliverExchange()).with(serverUUID)
    }

    fun DELIVER_QUEUE_NAME(): String {
        return DELIVER_QUEUE_NAME(serverUUID)
    }

    companion object {
        const val SAVE_EXCHANGE_NAME = "teamup.direct.save"
        const val SAVE_QUEUE_NAME = "teamup.message.save"

        const val DELIVER_EXCHANGE_NAME = "teamup.direct.deliver"

        fun DELIVER_QUEUE_NAME(uuid: String): String {
            return "teamup.message.deliver.$uuid"
        }

    }

}