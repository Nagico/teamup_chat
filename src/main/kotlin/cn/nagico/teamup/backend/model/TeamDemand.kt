package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("zq_team_demand")
class TeamDemand : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var number: Int? = null

    var detail: String? = null

    var roleId: Long? = null

    var teamId: Long? = null

    override fun toString(): String {
        return "ZqTeamDemand{" +
        "id=" + id +
        ", number=" + number +
        ", detail=" + detail +
        ", roleId=" + roleId +
        ", teamId=" + teamId +
        "}"
    }
}
