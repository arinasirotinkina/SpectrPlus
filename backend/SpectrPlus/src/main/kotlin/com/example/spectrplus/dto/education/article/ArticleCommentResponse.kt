package com.example.spectrplus.dto.education.article

import java.time.LocalDateTime

data class ArticleCommentResponse(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val content: String,
    val createdAt: LocalDateTime
)