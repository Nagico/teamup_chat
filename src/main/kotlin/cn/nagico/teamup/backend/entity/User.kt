package cn.nagico.teamup.backend.entity

import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.constant.status.UserStatus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable

@Configurable
data class User (
    val id: Long,
) {
    @Autowired
    lateinit var userCacheManager: UserCacheManager

    var status: UserStatus
        get() = userCacheManager.getUserStatusCache(id)
        set(value) {
            if (value == UserStatus.Online) {
                userCacheManager.userOnline(id) || throw RuntimeException("User $id is already online")
            } else {
                userCacheManager.setUserStatusCache(id, value)
            }
        }
}