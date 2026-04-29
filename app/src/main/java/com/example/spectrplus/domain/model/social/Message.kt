package com.example.spectrplus.domain.model.social

data class Message(
    val id: Long,
    val chatId: Long,
    val senderId: Long,
    val senderProfessionalLabel: String?,
    val content: String,
    val createdAt: String
)