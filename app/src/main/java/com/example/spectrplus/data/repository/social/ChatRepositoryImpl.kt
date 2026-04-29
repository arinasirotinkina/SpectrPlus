package com.example.spectrplus.data.repository.social

import com.example.spectrplus.data.api.social.ChatApi
import com.example.spectrplus.data.dto.social.SendMessageRequest
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.domain.repository.social.ChatRepository
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val api: ChatApi
) : ChatRepository {

    override suspend fun getChats() =
        api.getChats().map { it.toDomain() }

    override suspend fun openChat(userId: Long) =
        api.openChat(userId)

    override suspend fun getMessages(chatId: Long) =
        api.getMessages(chatId).map { it.toDomain() }

    override suspend fun sendMessage(chatId: Long, text: String) {
        api.sendMessage(chatId, SendMessageRequest(text))
    }
}