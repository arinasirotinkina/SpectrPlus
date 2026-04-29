package com.example.spectrplus.data.api.education

import com.example.spectrplus.data.dto.articles.VideoDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface VideoApi {

    @GET("api/videos")
    suspend fun getVideos(
        @Query("category") category: String?,
        @Query("maxDuration") maxDuration: Int?
    ): List<VideoDto>

    @GET("api/videos/favorites")
    suspend fun getFavorites(): List<VideoDto>

    @GET("api/videos/{id}")
    suspend fun getById(@Path("id") id: Long): VideoDto

    @POST("api/videos/{id}/favorite")
    suspend fun toggleFavorite(@Path("id") id: Long)

    @POST("api/videos/{id}/watched")
    suspend fun markWatched(@Path("id") id: Long)
}
