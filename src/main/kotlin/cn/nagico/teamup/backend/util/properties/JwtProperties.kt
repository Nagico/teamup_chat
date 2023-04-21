package cn.nagico.teamup.backend.util.properties

import jakarta.validation.constraints.NotEmpty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import org.springframework.validation.annotation.Validated

@Component
@Validated
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {
    @NotEmpty(message = "JwtSecret不能为空")
    var secret: String = ""
}