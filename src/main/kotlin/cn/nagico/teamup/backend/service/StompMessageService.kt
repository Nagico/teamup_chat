package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.manager.MessageCacheManager
import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.manager.MessageQueueManager
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

    @Autowired
    private lateinit var messageQueueManager: MessageQueueManager

    fun getMessage(messageId: UUID): StompMessage {
        return messageCacheManager.getMessageCache(messageId) ?: StompMessage(messageMapper.selectById(UUIDUtil.toHex(messageId)))
    }

    fun setMessage(message: StompMessage) {
        messageCacheManager.setMessageCache(message.id, message)
    }

    fun deleteMessage(messageId: UUID) {
        messageCacheManager.deleteMessageCache(messageId)
    }

    fun deliverMessage(message: StompMessage) {
        setMessage(message)
        messageQueueManager.sendStompMessage(message)
    }

}