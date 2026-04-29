package com.example.spectrplus.controller.education

import com.example.spectrplus.dto.education.article.ArticleCommentResponse
import com.example.spectrplus.dto.education.article.ArticleResponse
import com.example.spectrplus.dto.education.article.CreateArticleCommentRequest
import com.example.spectrplus.service.education.ArticleService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/articles")
class ArticleController(
    private val service: ArticleService
) {

    @GetMapping
    fun getArticles(
        authentication: Authentication,
        @RequestParam(required = false) query: String?,
        @RequestParam(required = false) category: String?
    ): List<ArticleResponse> {

        return service.getArticles(
            email = authentication.name,
            query = query,
            category = category
        )
    }

    @GetMapping("/favorites")
    fun getFavorites(
        authentication: Authentication
    ): List<ArticleResponse> {
        return service.getFavorites(authentication.name)
    }

    @GetMapping("/{id}")
    fun getById(
        authentication: Authentication,
        @PathVariable id: Long
    ): ArticleResponse {
        return service.getById(authentication.name, id)
    }

    @GetMapping("/{id}/related")
    fun getRelated(
        authentication: Authentication,
        @PathVariable id: Long
    ): List<ArticleResponse> {
        return service.getRelated(authentication.name, id)
    }

    @PostMapping("/{id}/favorite")
    fun toggleFavorite(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        service.toggleFavorite(authentication.name, id)
    }

    @GetMapping("/{id}/comments")
    fun getComments(
        @PathVariable id: Long
    ): List<ArticleCommentResponse> {
        return service.getComments(id)
    }

    @PostMapping("/{id}/comments")
    fun addComment(
        authentication: Authentication,
        @PathVariable id: Long,
        @RequestBody req: CreateArticleCommentRequest
    ): ArticleCommentResponse {
        return service.addComment(authentication.name, id, req)
    }
}