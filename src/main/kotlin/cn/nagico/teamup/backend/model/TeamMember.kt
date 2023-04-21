package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("zq_team_member")
class TeamMember : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var nickname: String? = null

    var degree: Int? = null

    var grade: Int? = null

    var introduction: String? = null

    var experience: String? = null

    var academyId: Long? = null

    var teamId: Long? = null

    var userId: Long? = null

    override fun toString(): String {
        return "TeamMember{" +
        "id=" + id +
        ", nickname=" + nickname +
        ", degree=" + degree +
        ", grade=" + grade +
        ", introduction=" + introduction +
        ", experience=" + experience +
        ", academyId=" + academyId +
        ", teamId=" + teamId +
        ", userId=" + userId +
        "}"
    }
}
