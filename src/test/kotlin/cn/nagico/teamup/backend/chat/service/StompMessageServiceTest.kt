package cn.nagico.teamup.backend.chat.service

import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
import cn.nagico.teamup.backend.service.StompMessageService
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import org.junit.jupiter.api.Test

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.util.AssertionErrors.assertEquals

@SpringBootTest
class StompMessageServiceTest {
    @Autowired
    private lateinit var stompMessageService: StompMessageService
}