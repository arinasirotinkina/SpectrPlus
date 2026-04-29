package com.example.spectrplus.data.mapper

import com.example.spectrplus.data.dto.articles.MaterialDto
import com.example.spectrplus.data.dto.articles.VideoDto
import com.example.spectrplus.domain.model.education.Material
import com.example.spectrplus.domain.model.education.Video

fun MaterialDto.toDomain(): Material {
    return Material(
        id = id,
        title = title,
        description = description,
        fileUrl = fileUrl,
        type = type,
        category = category
    )
}

fun VideoDto.toDomain(): Video {
    return Video(
        id = id,
        title = title,
        description = description,
        videoUrl = videoUrl,
        thumbnailUrl = thumbnailUrl,
        durationSeconds = durationSeconds,
        category = category,
        subtitlesUrl = subtitlesUrl,
        isFavorite = isFavorite,
        isWatched = isWatched
    )
}