package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.util.constant.UserStatus
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class UserServiceImplTest {
    @Mock
    private lateinit var userCacheManager: UserCacheManager

    @InjectMocks
    private lateinit var userServiceImpl: UserServiceImpl

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.openMocks(this)

        // set userServiceImpl.serverUUID
        PowerMockito.field(UserServiceImpl::class.java, "serverUUID").set(userServiceImpl, "1")
    }

    @Test
    fun testGetUserStatus_Online() {
        val userId = 123L
        `when`(userCacheManager.getUserServer(userId)).thenReturn("server123")

        val result = userServiceImpl.getUserStatus(userId)

        assertEquals(UserStatus.Online, result)
    }

    @Test
    fun testGetUserStatus_Offline() {
        val userId = 123L
        `when`(userCacheManager.getUserServer(userId)).thenReturn(null)

        val result = userServiceImpl.getUserStatus(userId)

        assertEquals(UserStatus.Offline, result)
    }

    @Test
    fun testOnline() {
        val userId = 123L

        userServiceImpl.online(userId)

        // Verify that the userCacheManager's online method was called with the correct arguments
        verify(userCacheManager).online(userId, "1")
    }

    @Test
    fun testOffline() {
        val userId = 123L

        userServiceImpl.offline(userId)

        // Verify that the userCacheManager's offline method was called with the correct argument
        verify(userCacheManager).offline(userId)
    }

    @Test
    fun testGetUserServer() {
        val userId = 123L
        val expectedServer = "server123"
        `when`(userCacheManager.getUserServer(userId)).thenReturn(expectedServer)

        val result = userServiceImpl.getUserServer(userId)

        assertEquals(expectedServer, result)
    }
}