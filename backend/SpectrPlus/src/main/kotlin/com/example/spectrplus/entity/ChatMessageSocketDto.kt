package com.example.spectrplus.entity

data class ChatMessageSocketDto(
    val chatId: Long = 0,
    val receiverId: Long = 0,
    val content: String = ""
)