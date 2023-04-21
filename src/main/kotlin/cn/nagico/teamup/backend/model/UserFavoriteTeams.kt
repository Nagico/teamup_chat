package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("zq_user_favorite_teams")
class UserFavoriteTeams : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var userId: Long? = null

    var teamId: Long? = null

    override fun toString(): String {
        return "ZqUserFavoriteTeams{" +
        "id=" + id +
        ", userId=" + userId +
        ", teamId=" + teamId +
        "}"
    }
}
