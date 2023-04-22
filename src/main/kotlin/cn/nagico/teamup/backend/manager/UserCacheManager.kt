package cn.nagico.teamup.backend.manager


import cn.nagico.teamup.backend.constant.status.UserStatus
import cn.nagico.teamup.backend.constant.RedisKey
import cn.nagico.teamup.backend.exception.StompAuthError
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

    fun getUserServer(userId: Long): String? {
        return redisTemplate.opsForValue()[RedisKey.userServerKey(userId)] as String?
    }

    private fun setUserServer(userId: Long, severUUID: String) {
        redisTemplate.opsForValue().set(
            RedisKey.userServerKey(userId),
            severUUID
        )
    }

    private fun deleteUserServer(userId: Long) {
        redisTemplate.delete(RedisKey.userServerKey(userId))
    }

    fun online(userId: Long, serverUUID: String) {
        val lock = redissonClient.getLock(RedisKey.userStatusLockKey(userId))
        lock.lock()
        try {
            getUserServer(userId)?.let {
                //throw StompAuthError("User $userId is already online")
                setUserServer(userId, serverUUID)
            } ?: setUserServer(userId, serverUUID)
        } finally {
            lock.unlock()
        }
    }

    fun offline(userId: Long) {
        val lock = redissonClient.getLock(RedisKey.userStatusLockKey(userId))
        lock.lock()
        try {
            val uuid = getUserServer(userId)
            if (uuid != null) {
                deleteUserServer(userId)
            }
        } finally {
            lock.unlock()
        }
    }
}