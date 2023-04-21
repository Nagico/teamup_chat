package cn.nagico.teamup.backend.entity

import cn.nagico.teamup.backend.manager.UserCacheManager
import cn.nagico.teamup.backend.constant.status.UserStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserTest {
    @Autowired
    private lateinit var userCacheManager: UserCacheManager

    @Test
    fun test() {
        val user = User(1, userCacheManager)
        user.status = UserStatus.Online
        assertEquals(UserStatus.Online, user.status)
    }
}