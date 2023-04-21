package cn.nagico.teamup.backend

import cn.nagico.teamup.backend.handler.StompChatHandler
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class Application {
    @Autowired
    lateinit var stompChatHandler: StompChatHandler
    @PostConstruct
    fun init() {
        handler = stompChatHandler
    }
    companion object {
        lateinit var handler: StompChatHandler
        @JvmStatic
        fun main(args: Array<String>) {
            runApplication<Application>(*args)
            while (true) {
                Thread.sleep(1000)
            }
        }
    }
}

fun main(args: Array<String>) {
    Application.main(args)
}
