package cn.nagico.teamup.backend.mapper

import cn.nagico.teamup.backend.model.Message
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MessageMapper : BaseMapper<Message>
