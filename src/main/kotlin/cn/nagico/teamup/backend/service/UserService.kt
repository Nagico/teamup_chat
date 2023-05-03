package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.util.constant.UserStatus

interface UserService {
    /**
     * 获取用户状态
     *
     * @param userId
     * @return 用户状态
     */
    fun getUserStatus(userId: Long): UserStatus

    /**
     * 用户上线
     *
     * @param userId
     */
    fun online(userId: Long)

    /**
     * 用户下线
     *
     * @param userId
     */
    fun offline(userId: Long)

    /**
     * 获取用户所在服务器
     *
     * @param userId
     * @return
     */
    fun getUserServer(userId: Long): String?
}