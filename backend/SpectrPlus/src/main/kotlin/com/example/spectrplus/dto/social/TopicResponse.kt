package com.example.spectrplus.dto.social

import java.time.LocalDateTime

data class TopicResponse(
    val id: Long,
    val title: String,
    val category: String,
    val authorId: Long,
    val authorName: String,
    val createdAt: LocalDateTime,
    val postsCount: Int = 0,
    val subscribersCount: Int = 0
)
