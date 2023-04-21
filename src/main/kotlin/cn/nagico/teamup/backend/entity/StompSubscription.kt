package cn.nagico.teamup.backend.entity

import cn.nagico.teamup.backend.entity.User
import io.netty.channel.Channel

/**
 * 订阅信息
 *
 * @param user 用户
 * @param channel 订阅channel
 */
data class StompSubscription(
    val user: User,
    val channel: Channel,
)