package cn.nagico.teamup.backend.constant

import java.util.UUID


object RedisKey {
    //region user
    fun userServerKey(userId: Long) = "user:userServer:userId$userId"
    fun userStatusLockKey(userId: Long) = "user:userStatusLock:userId$userId"

    //endregion

    //region Message
    fun messageKey(messageId: String) = "message:messageId$messageId"

    //endregion
}