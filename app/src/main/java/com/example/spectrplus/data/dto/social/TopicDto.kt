package com.example.spectrplus.data.dto.social

data class TopicDto(
    val id: Long,
    val title: String,
    val category: String,
    val authorId: Long,
    val authorName: String,
    val createdAt: String,
    val postsCount: Int = 0,
    val subscribersCount: Int = 0
)
