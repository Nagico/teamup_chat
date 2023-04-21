package cn.nagico.teamup.backend.manager


import cn.nagico.teamup.backend.constant.status.UserStatus
import cn.nagico.teamup.backend.constant.RedisKey
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component


@Component
class UserCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var redissonClient: RedissonClient

    fun getUserStatusCache(userId: Long): UserStatus {
        return redisTemplate.opsForValue()[RedisKey.userStatusKey(userId)] as? UserStatus ?: UserStatus.Offline
    }

    fun setUserStatusCache(userId: Long, status: UserStatus) {
        redisTemplate.opsForValue().set(
            /* key = */ RedisKey.userStatusKey(userId),
            /* value = */ status
        )
    }

    fun userOnline(userId: Long): Boolean {
        val lock = redissonClient.getLock(RedisKey.userStatusLockKey(userId))
        lock.lock()
        try {
            val status = getUserStatusCache(userId)
            return if (status == UserStatus.Offline) {
                setUserStatusCache(userId, UserStatus.Online)
                true
            } else {
                false
            }
        } finally {
            lock.unlock()
        }
    }
}