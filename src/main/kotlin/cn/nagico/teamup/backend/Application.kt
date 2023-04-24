package cn.nagico.teamup.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
    while (true) {
        Thread.sleep(1000)
    }
}
