package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.VideoWatched
import org.springframework.data.jpa.repository.JpaRepository

interface VideoWatchedRepository : JpaRepository<VideoWatched, Long> {

    fun findByUserId(userId: Long): List<VideoWatched>

    fun existsByUserIdAndVideoId(userId: Long, videoId: Long): Boolean
}
