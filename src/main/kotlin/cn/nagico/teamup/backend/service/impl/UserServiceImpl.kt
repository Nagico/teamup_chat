package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.util.constant.UserStatus
import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {
    @Autowired
    private lateinit var userCacheManager: cn.nagico.teamup.backend.cache.UserCacheManager

    @Autowired
    private lateinit var serverUUID: String

    /**
     * 获取用户状态
     *
     * @param userId
     * @return 用户状态
     */
    override fun getUserStatus(userId: Long): UserStatus {
        return userCacheManager.getUserServer(userId)?.let { UserStatus.Online } ?: UserStatus.Offline
    }

    /**
     * 用户上线
     *
     * @param userId
     */
    override fun online(userId: Long) {
        userCacheManager.online(userId, serverUUID)
    }

    /**
     * 用户下线
     *
     * @param userId
     */
    override fun offline(userId: Long) {
        userCacheManager.offline(userId)
    }

    /**
     * 获取用户所在服务器
     *
     * @param userId
     * @return
     */
    override fun getUserServer(userId: Long): String? {
        return userCacheManager.getUserServer(userId)
    }
}