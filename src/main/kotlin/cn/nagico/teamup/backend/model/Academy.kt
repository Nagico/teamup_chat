package cn.nagico.teamup.backend.model

import com.baomidou.mybatisplus.annotation.IdType
import com.baomidou.mybatisplus.annotation.TableId
import com.baomidou.mybatisplus.annotation.TableName
import java.io.Serializable


@TableName("zq_academy")
class Academy : Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    var id: Long? = null

    var level: Int? = null

    var name: String? = null

    var parentId: Long? = null

    override fun toString(): String {
        return "Academy{" +
        "id=" + id +
        ", level=" + level +
        ", name=" + name +
        ", parentId=" + parentId +
        "}"
    }
}
