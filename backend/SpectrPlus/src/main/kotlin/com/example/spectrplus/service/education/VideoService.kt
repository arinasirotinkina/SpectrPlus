package com.example.spectrplus.service.education

import com.example.spectrplus.dto.education.video.VideoResponse
import com.example.spectrplus.entity.education.Video
import com.example.spectrplus.entity.education.VideoCategory
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.entity.profile.VideoFavorite
import com.example.spectrplus.entity.profile.VideoWatched
import com.example.spectrplus.repository.education.VideoRepository
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.repository.profile.VideoFavoriteRepository
import com.example.spectrplus.repository.profile.VideoWatchedRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class VideoService(
    private val repository: VideoRepository,
    private val favoriteRepository: VideoFavoriteRepository,
    private val watchedRepository: VideoWatchedRepository,
    private val userRepository: UserRepository
) {

    fun getVideos(
        email: String,
        category: String?,
        maxDuration: Int?
    ): List<VideoResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val videos = when {
            category != null && maxDuration != null ->
                repository.findByCategoryAndDurationSecondsLessThanEqual(
                    VideoCategory.valueOf(category),
                    maxDuration
                )

            category != null ->
                repository.findByCategory(VideoCategory.valueOf(category))

            maxDuration != null ->
                repository.findByDurationSecondsLessThanEqual(maxDuration)

            else -> repository.findAll()
        }

        return mapVideos(user.id, videos)
    }

    fun getById(email: String, id: Long): VideoResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val video = repository.findById(id)
            .orElseThrow { RuntimeException("Video not found") }

        return mapVideo(user.id, video)
    }

    fun getFavorites(email: String): List<VideoResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val ids = favoriteRepository.findByUserId(user.id).map { it.videoId }

        if (ids.isEmpty()) return emptyList()

        val videos = repository.findAllById(ids)

        return mapVideos(user.id, videos)
    }

    fun toggleFavorite(email: String, videoId: Long) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val exists = favoriteRepository.existsByUserIdAndVideoId(user.id, videoId)

        if (exists) {
            favoriteRepository.deleteByUserIdAndVideoId(user.id, videoId)
        } else {
            favoriteRepository.save(
                VideoFavorite(userId = user.id, videoId = videoId)
            )
        }
    }

    fun markWatched(email: String, videoId: Long) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        if (!watchedRepository.existsByUserIdAndVideoId(user.id, videoId)) {
            watchedRepository.save(
                VideoWatched(userId = user.id, videoId = videoId)
            )
        }
    }

    private fun mapVideos(userId: Long, videos: List<Video>): List<VideoResponse> {

        val favoriteIds = favoriteRepository.findByUserId(userId)
            .map { it.videoId }
            .toSet()

        val watchedIds = watchedRepository.findByUserId(userId)
            .map { it.videoId }
            .toSet()

        return videos.map {
            VideoResponse(
                id = it.id,
                title = it.title,
                description = it.description,
                videoUrl = it.videoUrl,
                thumbnailUrl = it.thumbnailUrl,
                durationSeconds = it.durationSeconds,
                category = it.category.name,
                subtitlesUrl = it.subtitlesUrl,
                isFavorite = favoriteIds.contains(it.id),
                isWatched = watchedIds.contains(it.id),
                sourceAttribution = it.sourceAttribution
            )
        }
    }

    private fun mapVideo(userId: Long, video: Video): VideoResponse {
        val isFavorite = favoriteRepository.existsByUserIdAndVideoId(userId, video.id)
        val isWatched = watchedRepository.existsByUserIdAndVideoId(userId, video.id)
        return VideoResponse(
            id = video.id,
            title = video.title,
            description = video.description,
            videoUrl = video.videoUrl,
            thumbnailUrl = video.thumbnailUrl,
            durationSeconds = video.durationSeconds,
            category = video.category.name,
            subtitlesUrl = video.subtitlesUrl,
            isFavorite = isFavorite,
            isWatched = isWatched,
            sourceAttribution = video.sourceAttribution
        )
    }

    fun createBySpecialist(
        email: String,
        title: String,
        description: String,
        videoUrl: String,
        thumbnailUrl: String,
        durationSeconds: Int,
        category: String,
        sourceAttribution: String?
    ): VideoResponse {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        if (user.accountRole != AccountRole.SPECIALIST) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Только для специалистов")
        }
        val cat = try {
            VideoCategory.valueOf(category)
        } catch (_: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестная категория")
        }
        val saved = repository.save(
            Video(
                title = title.trim(),
                description = description.trim(),
                videoUrl = videoUrl.trim(),
                thumbnailUrl = thumbnailUrl.trim(),
                durationSeconds = durationSeconds,
                category = cat,
                subtitlesUrl = null,
                sourceAttribution = sourceAttribution?.trim()?.takeIf { it.isNotEmpty() }
            )
        )
        return mapVideo(user.id, saved)
    }
}
