package cn.nagico.teamup.backend.constant


object RedisKey {
    //region user
    fun userStatusKey(userId: Long) = "user:userStatus:userId$userId"
    fun userStatusLockKey(userId: Long) = "user:userStatusLock:userId$userId"

    //endregion
}