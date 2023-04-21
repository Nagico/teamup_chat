package cn.nagico.teamup.backend.mapper

import cn.nagico.teamup.backend.model.User
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface UserMapper : BaseMapper<User>
