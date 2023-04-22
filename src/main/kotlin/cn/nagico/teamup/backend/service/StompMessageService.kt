package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.manager.MessageCacheManager
import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
import cn.nagico.teamup.backend.manager.MessageQueueManager
import cn.nagico.teamup.backend.mapper.MessageMapper
import cn.nagico.teamup.backend.util.uuid.UUIDUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StompMessageService {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var messageCacheManager: MessageCacheManager

    @Autowired
    private lateinit var messageMapper: MessageMapper

    @Autowired
    private lateinit var messageQueueManager: MessageQueueManager

    @Autowired
    private lateinit var serverUUID: String

    /**
     * 获取消息
     *
     * @param messageId 消息id
     * @return stomp消息
     */
    fun getMessage(messageId: UUID): StompMessage {
        return getMessage(UUIDUtil.toHex(messageId))
    }

    /**
     * 获取消息
     *
     * @param messageId 消息id (hex格式，无 dashes)
     * @return stomp消息
     */
    fun getMessage(messageId: String): StompMessage {
        return messageCacheManager.getMessageCache(messageId) ?: StompMessage(messageMapper.selectById(messageId))
    }

    /**
     * 添加消息
     *
     * @param message stomp消息
     */
    fun setMessage(message: StompMessage) {
        messageCacheManager.setMessageCache(message)
    }

    /**
     * 删除消息
     *
     * @param messageId 消息id
     */
    fun deleteMessage(messageId: String) {
        messageCacheManager.deleteMessageCache(messageId)
    }

    /**
     * 消息投递
     *
     * @param message stomp消息
     */
    fun deliverMessage(message: StompMessage) {
        val target = userService.getUserServer(message.receiver) ?: return

        when (message.type) {
            StompMessageType.MESSAGE -> {
                setMessage(message)
                messageQueueManager.deliverStompMessage(target, message)
                messageQueueManager.saveStompMessage(message)
            }
            StompMessageType.ACK -> {
                //deleteMessage(message.id)
                messageQueueManager.deliverStompMessage(target, message)
            }
        }
    }

}