package com.example.spectrplus.data.dto.specialist

data class CreateSpecialistArticleBody(
    val title: String,
    val content: String,
    val category: String,
    val sourceAttribution: String? = null,
    val coverImageUrl: String? = null
)

data class CreateSpecialistVideoBody(
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val durationSeconds: Int,
    val category: String,
    val sourceAttribution: String? = null
)

data class CreateSpecialistMaterialBody(
    val title: String,
    val description: String,
    val fileUrl: String,
    val type: String,
    val category: String,
    val fileSize: Long,
    val sourceAttribution: String? = null
)
