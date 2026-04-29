package com.example.spectrplus.dto.education.article

data class CreateSpecialistArticleRequest(
    val title: String,
    val content: String,
    val category: String,
    val sourceAttribution: String? = null,
    val coverImageUrl: String? = null
)