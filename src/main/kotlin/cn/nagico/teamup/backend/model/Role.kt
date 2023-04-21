package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable

@TableName("zq_role")
class Role : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var name: String? = null

    var level: Int? = null

    var description: String? = null

    var parentId: Long? = null

    override fun toString(): String {
        return "ZqRole{" +
        "id=" + id +
        ", name=" + name +
        ", level=" + level +
        ", description=" + description +
        ", parentId=" + parentId +
        "}"
    }
}
