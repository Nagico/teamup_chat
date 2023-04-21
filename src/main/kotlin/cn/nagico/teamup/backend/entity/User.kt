package cn.nagico.teamup.backend.entity

import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.constant.status.UserStatus

data class User (val id: Long, val userCacheManager: UserCacheManager) {
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