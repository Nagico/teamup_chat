package cn.nagico.teamup.backend.util.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ServerConfig {
    @Bean
    fun serverUUID(): String {
        return java.util.UUID.randomUUID().toString()
    }
}