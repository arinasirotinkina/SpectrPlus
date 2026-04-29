package com.example.spectrplus.controller.social

import com.example.spectrplus.dto.social.ChatResponse
import com.example.spectrplus.dto.social.MessageResponse
import com.example.spectrplus.dto.social.SendMessageRequest
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.service.social.ChatService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(
    private val chatService: ChatService,
    private val userRepo: UserRepository
) {

    private fun getUser(authentication: Authentication) =
        userRepo.findByEmail(authentication.name)
            ?: throw RuntimeException("User not found")

    @GetMapping
    fun getChats(authentication: Authentication): List<ChatResponse> {
        val user = getUser(authentication)
        return chatService.getChats(user.id)
    }

    @PostMapping("/with/{userId}")
    fun openChat(
        authentication: Authentication,
        @PathVariable userId: Long
    ): Long {
        val user = getUser(authentication)
        val chat = chatService.getOrCreateChat(user.id, userId)
        return chat.id
    }

    @GetMapping("/{chatId}/messages")
    fun getMessages(
        authentication: Authentication,
        @PathVariable chatId: Long
    ): List<MessageResponse> {
        return chatService.getMessages(chatId)
    }

    @PostMapping("/{chatId}/messages")
    fun sendMessage(
        authentication: Authentication,
        @PathVariable chatId: Long,
        @RequestBody req: SendMessageRequest
    ) {
        val user = getUser(authentication)
        chatService.sendMessage(user.id, chatId, req)
    }
}