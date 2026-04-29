package com.example.spectrplus.data.repository.education

import com.example.spectrplus.data.api.education.ArticleApi
import com.example.spectrplus.data.dto.articles.CreateArticleCommentRequest
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.data.mapper.toEntity
import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment
import com.example.spectrplus.domain.repository.education.ArticleRepository
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val api: ArticleApi
) : ArticleRepository {

    override suspend fun getArticles(
        query: String?,
        category: String?
    ): List<Article> {
        return api.getArticles(query, category).map { it.toEntity() }
    }

    override suspend fun getFavorites(): List<Article> {
        return api.getFavorites().map { it.toEntity() }
    }

    override suspend fun getById(id: Long): Article {
        return api.getById(id).toEntity()
    }

    override suspend fun getRelated(id: Long): List<Article> {
        return api.getRelated(id).map { it.toEntity() }
    }

    override suspend fun toggleFavorite(id: Long) {
        api.toggleFavorite(id)
    }

    override suspend fun getComments(id: Long): List<ArticleComment> {
        return api.getComments(id).map { it.toDomain() }
    }

    override suspend fun addComment(id: Long, content: String): ArticleComment {
        return api.addComment(id, CreateArticleCommentRequest(content)).toDomain()
    }
}
