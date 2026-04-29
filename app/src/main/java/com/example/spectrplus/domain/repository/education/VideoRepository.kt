package com.example.spectrplus.domain.repository.education

import com.example.spectrplus.domain.model.education.Video

interface VideoRepository {
    suspend fun getVideos(category: String?, maxDuration: Int?): List<Video>
    suspend fun getFavorites(): List<Video>
    suspend fun getById(id: Long): Video
    suspend fun toggleFavorite(id: Long)
    suspend fun markWatched(id: Long)
}
