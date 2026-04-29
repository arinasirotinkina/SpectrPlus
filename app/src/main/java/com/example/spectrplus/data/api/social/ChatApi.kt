package com.example.spectrplus.data.api.social

import com.example.spectrplus.data.dto.social.ChatDto
import com.example.spectrplus.data.dto.social.MessageDto
import com.example.spectrplus.data.dto.social.SendMessageRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChatApi {

    @GET("api/chat")
    suspend fun getChats(): List<ChatDto>

    @POST("api/chat/with/{userId}")
    suspend fun openChat(
        @Path("userId") userId: Long
    ): Long

    @GET("api/chat/{chatId}/messages")
    suspend fun getMessages(
        @Path("chatId") chatId: Long
    ): List<MessageDto>

    @POST("api/chat/{chatId}/messages")
    suspend fun sendMessage(
        @Path("chatId") chatId: Long,
        @Body request: SendMessageRequest
    )
}