package com.example.spectrplus.domain.repository.education

import com.example.spectrplus.domain.model.education.Article
import com.example.spectrplus.domain.model.education.ArticleComment

interface ArticleRepository {
    suspend fun getArticles(query: String?, category: String?): List<Article>
    suspend fun getFavorites(): List<Article>
    suspend fun getById(id: Long): Article
    suspend fun getRelated(id: Long): List<Article>
    suspend fun toggleFavorite(id: Long)
    suspend fun getComments(id: Long): List<ArticleComment>
    suspend fun addComment(id: Long, content: String): ArticleComment
}
