package cn.nagico.teamup.backend.manager


import cn.nagico.teamup.backend.constant.RedisKey
import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
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
                //TODO throw StompAuthError("User $userId is already online")
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

    fun getUserUnreadMessages(userId: Long): List<String> {
        return redisTemplate.opsForList().range(RedisKey.userUnreceivedMessagesKey(userId), 0, -1)?.map { it as String } ?: listOf()
    }

    fun addUserUnreceivedMessage(message: StompMessage) {
        redisTemplate.opsForList().leftPush(RedisKey.userUnreceivedMessagesKey(message.receiver), message.id)
    }

    fun deleteUserUnreceivedMessage(userId: Long, messageId: String) {
        redisTemplate.opsForList().remove(RedisKey.userUnreceivedMessagesKey(userId), 0, messageId)
    }
}