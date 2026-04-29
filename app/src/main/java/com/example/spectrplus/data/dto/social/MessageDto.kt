package com.example.spectrplus.data.dto.social

data class MessageDto(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val senderProfessionalLabel: String? = null,
    val content: String,
    val createdAt: String
)