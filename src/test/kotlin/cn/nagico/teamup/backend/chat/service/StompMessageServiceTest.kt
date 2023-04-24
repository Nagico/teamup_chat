package cn.nagico.teamup.backend.chat.service

import cn.nagico.teamup.backend.service.StompMessageService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class StompMessageServiceTest {
    @Autowired
    private lateinit var stompMessageService: StompMessageService
}