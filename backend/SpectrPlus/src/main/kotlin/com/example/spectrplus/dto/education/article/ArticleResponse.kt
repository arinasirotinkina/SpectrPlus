package com.example.spectrplus.dto.education.article

data class ArticleResponse(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val category: String,
    val isFavorite: Boolean,
    val sourceAttribution: String? = null,
    val coverImageUrl: String? = null
)