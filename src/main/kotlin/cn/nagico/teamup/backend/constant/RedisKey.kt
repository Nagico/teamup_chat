package cn.nagico.teamup.backend.constant

import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
import java.util.UUID


object RedisKey {
    //region user
    fun userServerKey(userId: Long) = "user:userServer:userId$userId"
    fun userStatusLockKey(userId: Long) = "user:userStatusLock:userId$userId"

    fun userUnreadMessagesKey(userId: Long) = "user:userUnreadMessages:userId$userId"

    //endregion

    //region Message
    fun messageKey(messageId: String, type: StompMessageType = StompMessageType.MESSAGE) = "message:messageId:$type:$messageId"
    fun messageKey(message: StompMessage) = "message:messageId:${message.type}:${message.id}"

    //endregion
}