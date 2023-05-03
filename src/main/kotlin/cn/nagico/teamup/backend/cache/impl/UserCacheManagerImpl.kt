package cn.nagico.teamup.backend.cache.impl


import cn.nagico.teamup.backend.cache.constant.RedisKey
import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.util.annotation.CacheManager
import org.redisson.api.RedissonClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.RedisTemplate


@CacheManager
class UserCacheManagerImpl: cn.nagico.teamup.backend.cache.UserCacheManager {
    @Autowired
    private lateinit var redisTemplate: RedisTemplate<String, Any>

    @Autowired
    lateinit var redissonClient: RedissonClient

    override fun getUserServer(userId: Long): String? {
        return redisTemplate.opsForValue()[cn.nagico.teamup.backend.cache.constant.RedisKey.userServerKey(userId)] as String?
    }

    private fun setUserServer(userId: Long, severUUID: String) {
        redisTemplate.opsForValue().set(
            cn.nagico.teamup.backend.cache.constant.RedisKey.userServerKey(userId),
            severUUID
        )
    }

    private fun deleteUserServer(userId: Long) {
        redisTemplate.delete(cn.nagico.teamup.backend.cache.constant.RedisKey.userServerKey(userId))
    }

    override fun online(userId: Long, serverUUID: String) {
        val lock = redissonClient.getLock(cn.nagico.teamup.backend.cache.constant.RedisKey.userStatusLockKey(userId))
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

    override fun offline(userId: Long) {
        val lock = redissonClient.getLock(cn.nagico.teamup.backend.cache.constant.RedisKey.userStatusLockKey(userId))
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

    override fun getUserUnreceivedMessages(userId: Long): List<String> {
        return redisTemplate.opsForList().range(cn.nagico.teamup.backend.cache.constant.RedisKey.userUnreceivedMessagesKey(userId), 0, -1)?.map { it as String } ?: listOf()
    }

    override fun addUserUnreceivedMessage(message: StompMessage) {
        redisTemplate.opsForList().rightPush(cn.nagico.teamup.backend.cache.constant.RedisKey.userUnreceivedMessagesKey(message.receiver), message.id)
    }

    override fun deleteUserUnreceivedMessage(userId: Long, messageId: String) {
        redisTemplate.opsForList().remove(cn.nagico.teamup.backend.cache.constant.RedisKey.userUnreceivedMessagesKey(userId), 0, messageId)
    }
}