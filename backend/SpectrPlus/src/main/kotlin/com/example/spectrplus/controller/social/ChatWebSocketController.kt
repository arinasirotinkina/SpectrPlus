package com.example.spectrplus.controller.social

import com.example.spectrplus.dto.social.MessageResponse
import com.example.spectrplus.dto.social.SendMessageRequest
import com.example.spectrplus.entity.ChatMessageSocketDto
import com.example.spectrplus.service.social.ChatService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(
    private val chatService: ChatService,
    private val objectMapper: ObjectMapper
) : TextWebSocketHandler() {

    private val sessions = mutableMapOf<Long, MutableList<WebSocketSession>>()
    override fun afterConnectionEstablished(session: WebSocketSession) {
        val userId = getUserId(session)
        sessions.computeIfAbsent(userId) { mutableListOf() }
            .add(session)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {

        try {
            val dto = objectMapper.readValue(
                message.payload,
                ChatMessageSocketDto::class.java
            )

            val senderId = getUserId(session)

            val saved = chatService.sendMessage(
                senderId,
                dto.chatId,
                SendMessageRequest(dto.content)
            )

            val response = MessageResponse(
                id = saved.id,
                chatId = saved.chatId,
                senderId = senderId,
                senderProfessionalLabel = saved.senderProfessionalLabel,
                content = saved.content,
                createdAt = saved.createdAt.toString()
            )

            val json = objectMapper.writeValueAsString(response)
            sendToUser(dto.receiverId, json)
            sendToUser(senderId, json)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun sendToUser(userId: Long, message: String) {
        sessions[userId]?.forEach { session ->
            if (session.isOpen) {
                session.sendMessage(TextMessage(message))
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        sessions.values.forEach { it.remove(session) }
    }

    private fun getUserId(session: WebSocketSession): Long {
        val query = session.uri?.query ?: return 0
        val params = query.split("&")
            .mapNotNull {
                val parts = it.split("=")
                if (parts.size == 2) parts[0] to parts[1] else null
            }
            .toMap()

        return params["userId"]?.toLongOrNull() ?: 0
    }
}