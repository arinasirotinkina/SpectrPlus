package com.example.spectrplus.data.dto.social

data class PostDto(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val authorProfessionalLabel: String? = null,
    val content: String,
    val createdAt: String
)