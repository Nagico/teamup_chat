package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.manager.MessageCacheManager
import cn.nagico.teamup.backend.entity.StompMessage
import cn.nagico.teamup.backend.enums.StompMessageType
import cn.nagico.teamup.backend.manager.MessageQueueManager
import cn.nagico.teamup.backend.manager.UserCacheManager
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

    @Autowired
    private lateinit var userCacheManager: UserCacheManager

    /**
     * 获取消息
     *
     * @param messageId 消息id
     * @return stomp消息
     */
    fun getMessage(messageId: UUID, type: StompMessageType): StompMessage {
        return getMessage(UUIDUtil.toHex(messageId), type)
    }

    /**
     * 获取消息
     *
     * @param messageId 消息id (hex格式，无 dashes)
     * @return stomp消息
     */
    fun getMessage(messageId: String, type: StompMessageType): StompMessage {
        return messageCacheManager.getMessageCache(messageId, type) ?: StompMessage(messageMapper.selectById(messageId.replace("-", "")))
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
    fun deleteMessage(messageId: String, type: StompMessageType) {
        messageCacheManager.deleteMessageCache(messageId, type)
    }

    fun deleteMessage(message: StompMessage) {
        messageCacheManager.deleteMessageCache(message.id, message.type)
    }

    /**
     * 消息投递
     *
     * @param message stomp消息
     */
    fun deliverMessage(message: StompMessage) {
        val target = userService.getUserServer(message.receiver) ?: let {
            // 用户不在线
            setMessage(message)
            userCacheManager.addUserUnreadMessage(message)
            messageQueueManager.saveStompMessage(message)
            return
        }

        when (message.type) {
            StompMessageType.MESSAGE -> {
                setMessage(message)
                messageQueueManager.deliverStompMessage(target, message)
                messageQueueManager.saveStompMessage(message)
            }
            StompMessageType.ACK -> {
                deleteMessage(message.id, StompMessageType.MESSAGE)
                messageQueueManager.deliverStompMessage(target, message)
                messageQueueManager.saveStompMessage(message)
            }
        }
    }

    /**
     * 获取用户未读消息
     *
     * @param userId
     * @return
     */
    fun fetchUnreadMessages(userId: Long): List<StompMessage> {
        return userCacheManager.getUserUnreadMessages(userId).mapNotNull { key ->
            messageCacheManager.getMessageCacheByKey(key).also {
                if (it is StompMessage && it.type == StompMessageType.ACK)  // ACK消息无需确认，直接删除
                {
                    messageCacheManager.deleteMessageCache(it.id, StompMessageType.MESSAGE)
                    messageCacheManager.deleteMessageCacheByKey(key)
                }
            }
        }.also {
            userCacheManager.clearUserUnreadMessages(userId)
        }
    }

}