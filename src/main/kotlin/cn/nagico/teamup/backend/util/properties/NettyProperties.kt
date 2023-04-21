package cn.nagico.teamup.backend.util.properties

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "netty")
class NettyProperties {
    var host: String = "127.0.0.1"

    var port: Int = 8080

    var portSalve: Int = 18080

    var timeout: Int = 6000

    var worker: Int = 1

    var boss: Int = 1
}