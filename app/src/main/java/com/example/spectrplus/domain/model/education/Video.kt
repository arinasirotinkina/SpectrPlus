package com.example.spectrplus.domain.model.education

data class Video(
    val id: Long,
    val title: String,
    val description: String,
    val videoUrl: String,
    val thumbnailUrl: String,
    val durationSeconds: Int,
    val category: String,
    val subtitlesUrl: String?,
    val isFavorite: Boolean = false,
    val isWatched: Boolean = false
)
