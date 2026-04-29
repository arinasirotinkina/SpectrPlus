package com.example.spectrplus.domain.interactor.social

import com.example.spectrplus.domain.model.social.Post
import com.example.spectrplus.domain.model.social.Topic
import com.example.spectrplus.domain.repository.social.ForumRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForumInteractor @Inject constructor(
    private val repository: ForumRepository
) {
    suspend fun getTopics(category: String?): List<Topic> = repository.getTopics(category)

    suspend fun getTopic(id: Long): Topic = repository.getTopic(id)

    suspend fun createTopic(title: String, category: String, content: String): Topic =
        repository.createTopic(title, category, content)

    suspend fun getPosts(topicId: Long): List<Post> = repository.getPosts(topicId)

    suspend fun addPost(topicId: Long, content: String): Post =
        repository.addPost(topicId, content)

    suspend fun editPost(postId: Long, content: String): Post =
        repository.editPost(postId, content)

    suspend fun deletePost(postId: Long) = repository.deletePost(postId)

    suspend fun subscribe(topicId: Long) = repository.subscribe(topicId)

    suspend fun unsubscribe(topicId: Long) = repository.unsubscribe(topicId)

    suspend fun isSubscribed(topicId: Long): Boolean = repository.isSubscribed(topicId)
}
