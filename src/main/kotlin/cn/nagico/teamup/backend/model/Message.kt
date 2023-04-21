package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDateTime

@TableName("zq_message")
class Message : Serializable {

    @TableId(value = "id")
    var id: String? = null

    var content: String? = null

    var type: Int? = null

    var isRead: Boolean? = null

    var createTime: LocalDateTime? = null

    var receiverId: Long? = null

    var senderId: Long? = null

    override fun toString(): String {
        return "ZqMessage{" +
        "id=" + id +
        ", content=" + content +
        ", type=" + type +
        ", read=" + isRead +
        ", createTime=" + createTime +
        ", receiverId=" + receiverId +
        ", senderId=" + senderId +
        "}"
    }
}
