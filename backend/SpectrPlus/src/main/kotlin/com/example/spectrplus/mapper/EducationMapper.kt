package com.example.spectrplus.mapper

import com.example.spectrplus.dto.education.material.MaterialResponse
import com.example.spectrplus.dto.education.video.VideoResponse
import com.example.spectrplus.entity.education.Material
import com.example.spectrplus.entity.education.Video

fun Material.toDto() = MaterialResponse(
    id = id,
    title = title,
    description = description,
    fileUrl = fileUrl,
    type = type.name,
    category = category.name,
    fileSize = fileSize,
    sourceAttribution = sourceAttribution
)
fun Video.toDto() = VideoResponse(
    id = id,
    title = title,
    description = description,
    videoUrl = videoUrl,
    thumbnailUrl = thumbnailUrl,
    durationSeconds = durationSeconds,
    category = category.name,
    subtitlesUrl = subtitlesUrl,
    isFavorite = false,
    isWatched = false,
    sourceAttribution = sourceAttribution
)