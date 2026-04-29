package com.example.spectrplus.domain.model.education

data class ArticleComment(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val content: String,
    val createdAt: String
)
