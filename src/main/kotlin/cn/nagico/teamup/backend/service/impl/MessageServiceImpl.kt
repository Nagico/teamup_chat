package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.mapper.MessageMapper
import cn.nagico.teamup.backend.model.Message
import cn.nagico.teamup.backend.service.MessageService
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import org.springframework.stereotype.Service

@Service
class MessageServiceImpl : ServiceImpl<MessageMapper, Message>(), MessageService {

}
