package cn.nagico.teamup.backend.cache


import cn.nagico.teamup.backend.stomp.entity.message.StompMessage


interface UserCacheManager {
    /**
     * 获取用户所在服务器
     *
     * @param userId 用户id
     * @return 服务器uuid 若不存在则为空
     */
    fun getUserServer(userId: Long): String?

    /**
     * 用户上线
     *
     * @param userId 用户id
     * @param serverUUID 服务器uuid
     */
    fun online(userId: Long, serverUUID: String)

    /**
     * 用户下线
     *
     * @param userId 用户id
     */
    fun offline(userId: Long)

    /**
     * 获取用户未收到的消息id列表
     *
     * @param userId 用户id
     * @return 未收到消息id列表
     */
    fun getUserUnreceivedMessages(userId: Long): List<String>

    /**
     * 添加未收到消息
     *
     * @param message stomp消息
     */
    fun addUserUnreceivedMessage(message: StompMessage)

    /**
     * 删除未收到消息
     *
     * @param userId 用户id
     * @param messageId 消息id
     */
    fun deleteUserUnreceivedMessage(userId: Long, messageId: String)
}