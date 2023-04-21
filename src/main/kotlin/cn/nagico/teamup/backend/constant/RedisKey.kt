package cn.nagico.teamup.backend.constant

import java.util.UUID


object RedisKey {
    //region user
    fun userStatusKey(userId: Long) = "user:userStatus:userId$userId"
    fun userStatusLockKey(userId: Long) = "user:userStatusLock:userId$userId"

    //endregion

    //region Message
    fun messageKey(messageId: UUID) = "message:messageId$messageId"

    //endregion
}