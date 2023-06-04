package cn.nagico.teamup.backend.cache


import cn.nagico.teamup.backend.stomp.entity.message.StompMessage


interface MessageCacheManager {
    fun getMessageCache(messageId: String): StompMessage?
    fun addMessageCache(message: StompMessage)
    fun deleteMessageCache(messageId: String)
}