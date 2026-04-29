package com.example.spectrplus.domain.interactor.social

import com.example.spectrplus.domain.model.social.Chat
import com.example.spectrplus.domain.model.social.Message
import com.example.spectrplus.domain.repository.social.ChatRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatInteractor @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun getChats(): List<Chat> = repository.getChats()

    suspend fun openChat(userId: Long): Long = repository.openChat(userId)

    suspend fun getMessages(chatId: Long): List<Message> = repository.getMessages(chatId)

    suspend fun sendMessage(chatId: Long, text: String) =
        repository.sendMessage(chatId, text)
}
