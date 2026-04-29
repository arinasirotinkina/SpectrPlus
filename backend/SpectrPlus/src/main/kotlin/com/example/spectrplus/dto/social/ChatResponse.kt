package com.example.spectrplus.dto.social

data class ChatResponse(
    val id: Long,
    val otherUserId: Long,
    val otherUserName: String,
    val lastMessage: String?
)