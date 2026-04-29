package com.example.spectrplus.domain.interactor.education

import com.example.spectrplus.domain.model.education.Video
import com.example.spectrplus.domain.repository.education.VideoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VideoInteractor @Inject constructor(
    private val repository: VideoRepository
) {
    suspend fun getVideos(category: String?, maxDuration: Int?): List<Video> =
        repository.getVideos(category, maxDuration)

    suspend fun getFavorites(): List<Video> = repository.getFavorites()

    suspend fun getById(id: Long): Video = repository.getById(id)

    suspend fun toggleFavorite(id: Long) = repository.toggleFavorite(id)

    suspend fun markWatched(id: Long) = repository.markWatched(id)
}
