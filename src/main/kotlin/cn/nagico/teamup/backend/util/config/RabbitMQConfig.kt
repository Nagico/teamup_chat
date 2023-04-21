package cn.nagico.teamup.backend.util.config

import cn.nagico.teamup.backend.util.annotation.Slf4j
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.FanoutExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Slf4j
@Configuration
class RabbitMQConfig {
    // Queue
    @Bean
    fun messageQueue(): Queue {
        return Queue(QUEUE_NAME, true)
    }

    // Exchange
    @Bean
    fun fanoutExchange(): FanoutExchange {
        return FanoutExchange(EXCHANGE_NAME)
    }

    // Binding
    @Bean
    fun bindingFanoutExchangeMessageQueue(): Binding {
        return BindingBuilder.bind(messageQueue()).to(fanoutExchange())
    }

    companion object {
        const val EXCHANGE_NAME = "teamup.fanout"
        const val QUEUE_NAME = "teamup.message"
    }

}