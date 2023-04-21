package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

/**
 * <p>
 * 
 * </p>
 *
 * @author nagico
 * @since 2023-04-18
 */
@TableName("zq_user_favorite_activities")
class UserFavoriteActivities : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var userId: Long? = null

    var activityId: Long? = null

    override fun toString(): String {
        return "ZqUserFavoriteActivities{" +
        "id=" + id +
        ", userId=" + userId +
        ", activityId=" + activityId +
        "}"
    }
}
