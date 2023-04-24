package cn.nagico.teamup.backend.entity.message

import cn.nagico.teamup.backend.enums.StompMessageContentType
import com.alibaba.fastjson.JSON

data class StompMessageContent(
    val type: StompMessageContentType,
    val content: String,
) {
    override fun toString(): String {
        return JSON.toJSONString(this)
    }
}