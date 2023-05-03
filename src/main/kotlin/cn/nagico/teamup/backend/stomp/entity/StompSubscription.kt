package cn.nagico.teamup.backend.stomp.entity

import io.netty.channel.Channel

/**
 * 订阅信息
 *
 * @param user 用户
 * @param channel 订阅channel
 */
data class StompSubscription(
    val user: Long,
    val channel: Channel,
)