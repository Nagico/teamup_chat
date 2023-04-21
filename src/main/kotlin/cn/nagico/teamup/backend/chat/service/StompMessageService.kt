package cn.nagico.teamup.backend.chat.service

import cn.nagico.teamup.backend.cache.MessageCacheManager
import cn.nagico.teamup.backend.chat.entity.StompMessage
import cn.nagico.teamup.backend.mapper.MessageMapper
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StompMessageService {
    @Autowired
    private lateinit var messageCacheManager: MessageCacheManager

    @Autowired
    private lateinit var messageMapper: MessageMapper

    fun getMessage(messageId: UUID): StompMessage {
        return messageCacheManager.getMessageCache(messageId) ?: StompMessage(messageMapper.selectById(UUIDUtil.toHex(messageId)))
    }

    fun setMessage(messageId: UUID, message: StompMessage) {
        messageCacheManager.setMessageCache(messageId, message)
    }

}