package com.example.spectrplus.domain.repository.social

import com.example.spectrplus.domain.model.social.Chat
import com.example.spectrplus.domain.model.social.Message

interface ChatRepository {
    suspend fun getChats(): List<Chat>
    suspend fun openChat(userId: Long): Long
    suspend fun getMessages(chatId: Long): List<Message>
    suspend fun sendMessage(chatId: Long, text: String)
}