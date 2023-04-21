package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable
import java.time.LocalDateTime
@TableName("zq_user")
class User : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var username: String? = null

    var password: String? = null

    var email: String? = null

    var isStaff: Boolean? = null

    var isActive: Boolean? = null

    var isSuperuser: Boolean? = null

    var phone: String? = null

    var createTime: LocalDateTime? = null

    var updateTime: LocalDateTime? = null

    var nickname: String? = null

    var degree: Int? = null

    var grade: Int? = null

    var introduction: String? = null

    var experience: String? = null

    var openid: String? = null

    var unionId: String? = null

    var name: String? = null

    var avatar: String? = null

    var contact: String? = null

    var studentId: String? = null

    var academyId: Long? = null

    override fun toString(): String {
        return "ZqUser{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", email=" + email +
        ", isStaff=" + isStaff +
        ", isActive=" + isActive +
        ", isSuperuser=" + isSuperuser +
        ", phone=" + phone +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", nickname=" + nickname +
        ", degree=" + degree +
        ", grade=" + grade +
        ", introduction=" + introduction +
        ", experience=" + experience +
        ", openid=" + openid +
        ", unionId=" + unionId +
        ", name=" + name +
        ", avatar=" + avatar +
        ", contact=" + contact +
        ", studentId=" + studentId +
        ", academyId=" + academyId +
        "}"
    }
}
