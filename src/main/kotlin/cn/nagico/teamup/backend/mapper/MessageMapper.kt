package cn.nagico.teamup.backend.mapper

import cn.nagico.teamup.backend.model.Message
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import com.alibaba.fastjson2.util.UUIDUtils
import com.baomidou.mybatisplus.core.mapper.BaseMapper
import org.apache.ibatis.annotations.Mapper
import java.util.UUID

@Mapper
interface MessageMapper : BaseMapper<Message>