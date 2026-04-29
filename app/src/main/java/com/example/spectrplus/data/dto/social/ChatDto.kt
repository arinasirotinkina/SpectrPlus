package com.example.spectrplus.data.dto.social

data class ChatDto(
    val id: Long,
    val otherUserId: Long,
    val otherUserName: String,
    val lastMessage: String?
)