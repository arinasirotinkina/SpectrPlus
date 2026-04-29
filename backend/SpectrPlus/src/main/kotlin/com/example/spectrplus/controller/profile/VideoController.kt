package com.example.spectrplus.controller.profile

import com.example.spectrplus.dto.education.video.VideoResponse
import com.example.spectrplus.service.education.VideoService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/videos")
class VideoController(
    private val service: VideoService
) {

    @GetMapping
    fun getVideos(
        authentication: Authentication,
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) maxDuration: Int?
    ): List<VideoResponse> {
        return service.getVideos(authentication.name, category, maxDuration)
    }

    @GetMapping("/favorites")
    fun getFavorites(
        authentication: Authentication
    ): List<VideoResponse> {
        return service.getFavorites(authentication.name)
    }

    @GetMapping("/{id}")
    fun getById(
        authentication: Authentication,
        @PathVariable id: Long
    ): VideoResponse {
        return service.getById(authentication.name, id)
    }

    @PostMapping("/{id}/favorite")
    fun toggleFavorite(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        service.toggleFavorite(authentication.name, id)
    }

    @PostMapping("/{id}/watched")
    fun markWatched(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        service.markWatched(authentication.name, id)
    }
}