package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDateTime

@TableName("zq_feedback_file")
class FeedbackFile : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var file: String? = null

    var createdTime: LocalDateTime? = null

    var feedbackId: Long? = null

    override fun toString(): String {
        return "FeedbackFile{" +
        "id=" + id +
        ", file=" + file +
        ", createdTime=" + createdTime +
        ", feedbackId=" + feedbackId +
        "}"
    }
}
