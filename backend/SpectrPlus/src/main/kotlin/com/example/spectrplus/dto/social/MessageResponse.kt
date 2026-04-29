package com.example.spectrplus.dto.social

import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val senderProfessionalLabel: String? = null,
    val content: String,
    val createdAt: String
)