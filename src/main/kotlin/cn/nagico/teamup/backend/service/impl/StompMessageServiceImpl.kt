package cn.nagico.teamup.backend.service.impl

import cn.nagico.teamup.backend.service.UserService

import cn.nagico.teamup.backend.cache.MessageCacheManager
import cn.nagico.teamup.backend.stomp.entity.message.StompMessage
import cn.nagico.teamup.backend.service.MessageQueueService
import cn.nagico.teamup.backend.cache.UserCacheManager
import cn.nagico.teamup.backend.service.StompMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StompMessageServiceImpl: StompMessageService {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var messageCacheManager: cn.nagico.teamup.backend.cache.MessageCacheManager

    @Autowired
    private lateinit var messageQueueService: MessageQueueService

    @Autowired
    private lateinit var serverUUID: String

    @Autowired
    private lateinit var userCacheManager: cn.nagico.teamup.backend.cache.UserCacheManager

    /**
     * 获取消息
     *
     * @param messageId 消息id (hex格式，无 dashes)
     * @return stomp消息
     */
    override fun getMessage(messageId: String): StompMessage {
        return messageCacheManager.getMessageCache(messageId)!!
    }

    /**
     * 消息投递
     *
     * @param message stomp消息
     */
    override fun deliverMessage(message: StompMessage) {
        messageCacheManager.addMessageCache(message)
        userCacheManager.addUserUnreceivedMessage(message)
        messageQueueService.saveStompMessage(message)

        val target = userService.getUserServer(message.receiver) ?: return
        messageQueueService.forwardStompMessage(target, message)  // 用户在线 转发消息
    }

    /**
     * 确认消息送达
     *
     * @param userId 用户id
     * @param messageId 消息id
     */
    override fun ackMessage(userId: Long, messageId: String) {
        userCacheManager.deleteUserUnreceivedMessage(userId, messageId)
        messageCacheManager.deleteMessageCache(messageId)
    }

    /**
     * 获取用户未接收消息
     *
     * @param userId
     * @return
     */
    override fun fetchUnreceivedMessages(userId: Long): List<StompMessage> {
        return userCacheManager.getUserUnreceivedMessages(userId).mapNotNull {
            messageCacheManager.getMessageCache(it)
        }
    }

}