package com.example.spectrplus.dto.social

import java.time.LocalDateTime

data class PostResponse(
    val id: Long,
    val authorId: Long,
    val authorName: String,
    val authorProfessionalLabel: String? = null,
    val content: String,
    val createdAt: LocalDateTime
)