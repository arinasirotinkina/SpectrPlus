package com.example.spectrplus.domain.model.education


data class Article(
    val id: Long,
    val title: String,
    val content: String,
    val author: String,
    val category: String,
    val isFavorite: Boolean
)