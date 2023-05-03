package cn.nagico.teamup.backend.stomp.entity.message

import cn.nagico.teamup.backend.stomp.constant.StompMessageContentType
import com.alibaba.fastjson.JSON

data class StompMessageContent(
    val type: StompMessageContentType,
    val content: String,
) {
    override fun toString(): String {
        return JSON.toJSONString(this)
    }
}