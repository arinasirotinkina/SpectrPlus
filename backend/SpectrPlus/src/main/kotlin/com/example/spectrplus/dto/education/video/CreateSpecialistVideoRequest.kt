package com.example.spectrplus.dto.education.video

data class CreateSpecialistVideoRequest(
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val durationSeconds: Int,
    val category: String,
    val sourceAttribution: String? = null
)