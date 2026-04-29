package com.example.spectrplus.domain.model.social

data class Chat(
    val id: Long,
    val otherUserId: Long,
    val otherUserName: String,
    val lastMessage: String?
)