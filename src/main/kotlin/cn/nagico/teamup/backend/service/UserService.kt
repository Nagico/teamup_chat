package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.constant.status.UserStatus
import cn.nagico.teamup.backend.manager.UserCacheManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService {
    @Autowired
    private lateinit var userCacheManager: UserCacheManager

    @Autowired
    private lateinit var serverUUID: String

    /**
     * 获取用户状态
     *
     * @param userId
     * @return 用户状态
     */
    fun getUserStatus(userId: Long): UserStatus {
        return userCacheManager.getUserServer(userId)?.let { UserStatus.Online } ?: UserStatus.Offline
    }

    /**
     * 用户上线
     *
     * @param userId
     */
    fun online(userId: Long) {
        userCacheManager.online(userId, serverUUID)
    }

    /**
     * 用户下线
     *
     * @param userId
     */
    fun offline(userId: Long) {
        userCacheManager.offline(userId)
    }

    /**
     * 获取用户所在服务器
     *
     * @param userId
     * @return
     */
    fun getUserServer(userId: Long): String? {
        return userCacheManager.getUserServer(userId)
    }
}