package com.example.spectrplus.data.api.education

import com.example.spectrplus.data.dto.articles.ArticleCommentDto
import com.example.spectrplus.data.dto.articles.ArticleDto
import com.example.spectrplus.data.dto.articles.CreateArticleCommentRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ArticleApi {

    @GET("articles")
    suspend fun getArticles(
        @Query("query") query: String?,
        @Query("category") category: String?
    ): List<ArticleDto>

    @GET("articles/favorites")
    suspend fun getFavorites(): List<ArticleDto>

    @GET("articles/{id}")
    suspend fun getById(@Path("id") id: Long): ArticleDto

    @GET("articles/{id}/related")
    suspend fun getRelated(@Path("id") id: Long): List<ArticleDto>

    @POST("articles/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: Long)

    @GET("articles/{id}/comments")
    suspend fun getComments(@Path("id") id: Long): List<ArticleCommentDto>

    @POST("articles/{id}/comments")
    suspend fun addComment(
        @Path("id") id: Long,
        @Body request: CreateArticleCommentRequest
    ): ArticleCommentDto
}
