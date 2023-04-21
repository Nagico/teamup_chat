package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalDateTime

@TableName("zq_activity")
class Activity : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var title: String? = null

    var introduction: String? = null

    var information: String? = null

    var logo: String? = null

    var startTime: LocalDate? = null

    var endTime: LocalDate? = null

    var timeNode: String? = null

    var createTime: LocalDateTime? = null

    var updateTime: LocalDateTime? = null

    override fun toString(): String {
        return "ZqActivity{" +
        "id=" + id +
        ", title=" + title +
        ", introduction=" + introduction +
        ", information=" + information +
        ", logo=" + logo +
        ", startTime=" + startTime +
        ", endTime=" + endTime +
        ", timeNode=" + timeNode +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        "}"
    }
}
