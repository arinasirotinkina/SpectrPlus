package com.example.spectrplus.domain.repository.social

import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic

interface ForumRepository {
    suspend fun getTopics(category: String?): List<Topic>
    suspend fun getTopic(id: Long): Topic
    suspend fun createTopic(title: String, category: String, content: String): Topic
    suspend fun getPosts(topicId: Long): List<Post>
    suspend fun addPost(topicId: Long, content: String): Post
    suspend fun editPost(postId: Long, content: String): Post
    suspend fun deletePost(postId: Long)
    suspend fun subscribe(topicId: Long)
    suspend fun unsubscribe(topicId: Long)
    suspend fun isSubscribed(topicId: Long): Boolean
}
