package com.example.spectrplus.controller.education

import com.example.spectrplus.dto.education.article.ArticleResponse
import com.example.spectrplus.dto.education.article.CreateSpecialistArticleRequest
import com.example.spectrplus.dto.education.material.CreateSpecialistMaterialRequest
import com.example.spectrplus.dto.education.video.CreateSpecialistVideoRequest
import com.example.spectrplus.dto.education.material.MaterialResponse
import com.example.spectrplus.dto.education.video.VideoResponse
import com.example.spectrplus.service.education.ArticleService
import com.example.spectrplus.service.education.MaterialService
import com.example.spectrplus.service.education.VideoService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/specialist/content")
class SpecialistContentController(
    private val articleService: ArticleService,
    private val videoService: VideoService,
    private val materialService: MaterialService
) {

    @PostMapping("/articles")
    fun createArticle(
        authentication: Authentication,
        @RequestBody body: CreateSpecialistArticleRequest
    ): ArticleResponse {
        return articleService.createBySpecialist(
            email = authentication.name,
            title = body.title,
            content = body.content,
            category = body.category,
            sourceAttribution = body.sourceAttribution,
            coverImageUrl = body.coverImageUrl
        )
    }

    @PostMapping("/videos")
    fun createVideo(
        authentication: Authentication,
        @RequestBody body: CreateSpecialistVideoRequest
    ): VideoResponse {
        return videoService.createBySpecialist(
            email = authentication.name,
            title = body.title,
            description = body.description,
            videoUrl = body.videoUrl,
            thumbnailUrl = body.thumbnailUrl,
            durationSeconds = body.durationSeconds,
            category = body.category,
            sourceAttribution = body.sourceAttribution
        )
    }

    @PostMapping("/materials")
    fun createMaterial(
        authentication: Authentication,
        @RequestBody body: CreateSpecialistMaterialRequest
    ): MaterialResponse {
        return materialService.createBySpecialist(
            email = authentication.name,
            title = body.title,
            description = body.description,
            fileUrl = body.fileUrl,
            type = body.type,
            category = body.category,
            fileSize = body.fileSize,
            sourceAttribution = body.sourceAttribution
        )
    }
}