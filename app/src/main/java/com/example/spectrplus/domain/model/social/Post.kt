package com.example.spectrplus.domain.model.social

data class Post(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val authorProfessionalLabel: String?,
    val content: String,
    val createdAt: String
)