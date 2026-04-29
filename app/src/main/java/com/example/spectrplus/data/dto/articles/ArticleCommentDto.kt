package com.example.spectrplus.data.dto.articles

data class ArticleCommentDto(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val content: String,
    val createdAt: String
)

data class CreateArticleCommentRequest(
    val content: String
)
