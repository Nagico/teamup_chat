package cn.nagico.teamup.backend.cache.constant


object RedisKey {
    //region user
    fun userServerKey(userId: Long) = "user:userServer:userId$userId"
    fun userStatusLockKey(userId: Long) = "user:userStatusLock:userId$userId"

    fun userUnreceivedMessagesKey(userId: Long) = "user:userUnreceivedMessages:userId$userId"

    //endregion

    //region Message
    fun messageKey(messageId: String) = "message:messageId:$messageId"

    //endregion
}