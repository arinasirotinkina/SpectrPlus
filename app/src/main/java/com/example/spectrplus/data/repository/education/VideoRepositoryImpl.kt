package com.example.spectrplus.data.repository.education

import com.example.spectrplus.data.api.education.VideoApi
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.domain.model.education.Video
import com.example.spectrplus.domain.repository.education.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: VideoApi
) : VideoRepository {

    override suspend fun getVideos(
        category: String?,
        maxDuration: Int?
    ): List<Video> {
        return api.getVideos(category, maxDuration).map { it.toDomain() }
    }

    override suspend fun getFavorites(): List<Video> {
        return api.getFavorites().map { it.toDomain() }
    }

    override suspend fun getById(id: Long): Video {
        return api.getById(id).toDomain()
    }

    override suspend fun toggleFavorite(id: Long) {
        api.toggleFavorite(id)
    }

    override suspend fun markWatched(id: Long) {
        api.markWatched(id)
    }
}
