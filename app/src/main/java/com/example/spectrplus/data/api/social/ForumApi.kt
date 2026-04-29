package com.example.spectrplus.data.api.social

import com.example.spectrplus.data.dto.social.CreatePostRequest
import com.example.spectrplus.data.dto.social.CreateTopicRequest
import com.example.spectrplus.data.dto.social.PostDto
import com.example.spectrplus.data.dto.social.TopicDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ForumApi {

    @GET("api/forum/topics")
    suspend fun getTopics(
        @Query("category") category: String?
    ): List<TopicDto>

    @GET("api/forum/topics/{id}")
    suspend fun getTopic(
        @Path("id") id: Long
    ): TopicDto

    @POST("api/forum/topics")
    suspend fun createTopic(
        @Body request: CreateTopicRequest
    ): TopicDto

    @GET("api/forum/topics/{id}/posts")
    suspend fun getPosts(
        @Path("id") id: Long
    ): List<PostDto>

    @POST("api/forum/topics/{id}/posts")
    suspend fun addPost(
        @Path("id") id: Long,
        @Body request: CreatePostRequest
    ): PostDto

    @PUT("api/forum/posts/{id}")
    suspend fun editPost(
        @Path("id") id: Long,
        @Body request: CreatePostRequest
    ): PostDto

    @DELETE("api/forum/posts/{id}")
    suspend fun deletePost(
        @Path("id") id: Long
    )

    @POST("api/forum/topics/{id}/subscribe")
    suspend fun subscribe(
        @Path("id") id: Long
    )

    @DELETE("api/forum/topics/{id}/subscribe")
    suspend fun unsubscribe(
        @Path("id") id: Long
    )

    @GET("api/forum/topics/{id}/subscribed")
    suspend fun isSubscribed(
        @Path("id") id: Long
    ): Map<String, Boolean>
}
