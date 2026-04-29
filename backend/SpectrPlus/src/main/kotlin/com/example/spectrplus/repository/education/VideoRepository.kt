package com.example.spectrplus.repository.education

import com.example.spectrplus.entity.education.VideoCategory
import com.example.spectrplus.entity.education.Video
import org.springframework.data.jpa.repository.JpaRepository

interface VideoRepository : JpaRepository<Video, Long> {

    fun findByCategory(category: VideoCategory): List<Video>

    fun findByDurationSecondsLessThanEqual(duration: Int): List<Video>

    fun findByCategoryAndDurationSecondsLessThanEqual(
        category: VideoCategory,
        duration: Int
    ): List<Video>
}