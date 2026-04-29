package com.example.spectrplus.data.dto.articles

data class ArticleDto(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val category: String,
    val isFavorite: Boolean = false,
    val sourceAttribution: String? = null,
    val coverImageUrl: String? = null
)