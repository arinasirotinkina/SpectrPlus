package com.example.spectrplus.data.repository.social

import com.example.spectrplus.data.api.social.ForumApi
import com.example.spectrplus.data.dto.social.CreatePostRequest
import com.example.spectrplus.data.dto.social.CreateTopicRequest
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic
import com.example.spectrplus.domain.repository.social.ForumRepository
import javax.inject.Inject

class ForumRepositoryImpl @Inject constructor(
    private val api: ForumApi
) : ForumRepository {

    override suspend fun getTopics(category: String?) =
        api.getTopics(category).map { it.toDomain() }

    override suspend fun getTopic(id: Long): Topic =
        api.getTopic(id).toDomain()

    override suspend fun createTopic(title: String, category: String, content: String): Topic =
        api.createTopic(CreateTopicRequest(title, category, content)).toDomain()

    override suspend fun getPosts(topicId: Long) =
        api.getPosts(topicId).map { it.toDomain() }

    override suspend fun addPost(topicId: Long, content: String): Post =
        api.addPost(topicId, CreatePostRequest(content)).toDomain()

    override suspend fun editPost(postId: Long, content: String): Post =
        api.editPost(postId, CreatePostRequest(content)).toDomain()

    override suspend fun deletePost(postId: Long) {
        api.deletePost(postId)
    }

    override suspend fun subscribe(topicId: Long) {
        api.subscribe(topicId)
    }

    override suspend fun unsubscribe(topicId: Long) {
        api.unsubscribe(topicId)
    }

    override suspend fun isSubscribed(topicId: Long): Boolean =
        api.isSubscribed(topicId)["subscribed"] ?: false
}
