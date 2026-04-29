package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.VideoFavorite
import org.springframework.data.jpa.repository.JpaRepository

interface VideoFavoriteRepository : JpaRepository<VideoFavorite, Long> {

    fun findByUserId(userId: Long): List<VideoFavorite>

    fun existsByUserIdAndVideoId(userId: Long, videoId: Long): Boolean

    fun deleteByUserIdAndVideoId(userId: Long, videoId: Long)
}
