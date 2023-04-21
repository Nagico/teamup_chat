package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDateTime
@TableName("zq_feedback")
class Feedback : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var content: String? = null

    var type: Int? = null

    var createdTime: LocalDateTime? = null

    var updatedTime: LocalDateTime? = null

    var userId: Long? = null

    override fun toString(): String {
        return "ZqFeedback{" +
        "id=" + id +
        ", content=" + content +
        ", type=" + type +
        ", createdTime=" + createdTime +
        ", updatedTime=" + updatedTime +
        ", userId=" + userId +
        "}"
    }
}
