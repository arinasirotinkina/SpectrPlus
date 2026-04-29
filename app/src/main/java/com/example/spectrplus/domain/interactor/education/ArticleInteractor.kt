package com.example.spectrplus.domain.interactor.education

import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment
import com.example.spectrplus.domain.repository.education.ArticleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ArticleInteractor @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend fun getArticles(query: String?, category: String?): List<Article> =
        repository.getArticles(query, category)

    suspend fun getFavorites(): List<Article> = repository.getFavorites()

    suspend fun getById(id: Long): Article = repository.getById(id)

    suspend fun getRelated(id: Long): List<Article> = repository.getRelated(id)

    suspend fun toggleFavorite(id: Long) = repository.toggleFavorite(id)

    suspend fun getComments(id: Long): List<ArticleComment> = repository.getComments(id)

    suspend fun addComment(articleId: Long, content: String): ArticleComment =
        repository.addComment(articleId, content)
}
