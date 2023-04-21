package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDateTime

@TableName("zq_team")
class Team : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var name: String? = null

    var introduction: String? = null

    var teacher: String? = null

    var contact: String? = null

    var activityId: Long? = null

    var leaderId: Long? = null

    var public: Boolean? = null

    var createTime: LocalDateTime? = null

    var updateTime: LocalDateTime? = null

    override fun toString(): String {
        return "ZqTeam{" +
        "id=" + id +
        ", name=" + name +
        ", introduction=" + introduction +
        ", teacher=" + teacher +
        ", contact=" + contact +
        ", activityId=" + activityId +
        ", leaderId=" + leaderId +
        ", public=" + public +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}"
    }
}
