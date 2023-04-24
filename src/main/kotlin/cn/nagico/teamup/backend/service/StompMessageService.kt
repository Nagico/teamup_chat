package cn.nagico.teamup.backend.service

import cn.nagico.teamup.backend.manager.MessageCacheManager
import cn.nagico.teamup.backend.entity.message.StompMessage
import cn.nagico.teamup.backend.manager.MessageQueueManager
import cn.nagico.teamup.backend.manager.UserCacheManager
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
        return messageCacheManager.getMessageCache(messageId)!!
    }

    /**
     * 消息投递
     *
     * @param message stomp消息
     */
    fun deliverMessage(message: StompMessage) {
        messageCacheManager.addMessageCache(message)
        userCacheManager.addUserUnreceivedMessage(message)
        messageQueueManager.saveStompMessage(message)

        val target = userService.getUserServer(message.receiver) ?: return
        messageQueueManager.forwardStompMessage(target, message)  // 用户在线 转发消息
    }

    /**
     * 确认消息送达
     *
     * @param userId 用户id
     * @param messageId 消息id
     */
    fun ackMessage(userId: Long, messageId: String) {
        userCacheManager.deleteUserUnreceivedMessage(userId, messageId)
        messageCacheManager.deleteMessageCache(messageId)
    }

    /**
     * 获取用户未接收消息
     *
     * @param userId
     * @return
     */
    fun fetchUnreceivedMessages(userId: Long): List<StompMessage> {
        return userCacheManager.getUserUnreadMessages(userId).mapNotNull {
            messageCacheManager.getMessageCache(it)
        }
    }

}