package com.example.spectrplus.dto.education.video

data class VideoResponse(
    val id: Long,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val durationSeconds: Int,
    val category: String,
    val subtitlesUrl: String?,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false,
    val sourceAttribution: String? = null
)