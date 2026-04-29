package com.example.spectrplus.service.social

import com.example.spectrplus.dto.social.CreatePostRequest
import com.example.spectrplus.dto.social.CreateTopicRequest
import com.example.spectrplus.dto.social.PostResponse
import com.example.spectrplus.dto.social.TopicResponse
import com.example.spectrplus.entity.social.ForumCategory
import com.example.spectrplus.entity.social.PostEntity
import com.example.spectrplus.entity.social.TopicEntity
import com.example.spectrplus.entity.social.TopicSubscription
import com.example.spectrplus.mapper.toDto
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.repository.social.PostRepository
import com.example.spectrplus.repository.social.SubscriptionRepository
import com.example.spectrplus.repository.social.TopicRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ForumService(
    private val topicRepo: TopicRepository,
    private val postRepo: PostRepository,
    private val subRepo: SubscriptionRepository,
    private val userRepo: UserRepository
) {

    private fun professionalLabel(userId: Long): String? {
        val u = userRepo.findById(userId).orElse(null) ?: return null
        return if (u.accountRole == AccountRole.SPECIALIST) u.specialistProfession else null
    }

    fun getTopics(category: String?): List<TopicResponse> {
        val topics = if (category != null)
            topicRepo.findByCategory(ForumCategory.valueOf(category))
        else
            topicRepo.findAll()

        return topics.map {
            it.toDto(
                postsCount = postRepo.countByTopicId(it.id).toInt(),
                subscribersCount = subRepo.countByTopicId(it.id).toInt()
            )
        }
    }

    fun getTopic(id: Long): TopicResponse {
        val topic = topicRepo.findById(id)
            .orElseThrow { RuntimeException("Topic not found") }

        return topic.toDto(
            postsCount = postRepo.countByTopicId(topic.id).toInt(),
            subscribersCount = subRepo.countByTopicId(topic.id).toInt()
        )
    }

    fun createTopic(userId: Long, userName: String, req: CreateTopicRequest): TopicResponse {

        val topic = topicRepo.save(
            TopicEntity(
                title = req.title,
                category = ForumCategory.valueOf(req.category),
                authorId = userId,
                authorName = userName
            )
        )

        postRepo.save(
            PostEntity(
                topicId = topic.id,
                authorId = userId,
                authorName = userName,
                authorProfessionalLabel = professionalLabel(userId),
                content = req.content
            )
        )

        return topic.toDto(postsCount = 1, subscribersCount = 0)
    }

    fun getPosts(topicId: Long): List<PostResponse> {
        return postRepo.findByTopicId(topicId)
            .map { it.toDto() }
    }

    fun addPost(userId: Long, userName: String, topicId: Long, req: CreatePostRequest): PostResponse {
        val saved = postRepo.save(
            PostEntity(
                topicId = topicId,
                authorId = userId,
                authorName = userName,
                authorProfessionalLabel = professionalLabel(userId),
                content = req.content
            )
        )
        return saved.toDto()
    }

    fun editPost(userId: Long, postId: Long, content: String): PostResponse {
        val post = postRepo.findById(postId)
            .orElseThrow { RuntimeException("Post not found") }

        if (post.authorId != userId) throw RuntimeException("Forbidden")

        post.content = content
        return postRepo.save(post).toDto()
    }

    fun deletePost(userId: Long, postId: Long) {
        val post = postRepo.findById(postId)
            .orElseThrow { RuntimeException("Post not found") }

        if (post.authorId != userId) throw RuntimeException("Forbidden")

        postRepo.deleteById(postId)
    }

    fun subscribe(userId: Long, topicId: Long) {
        if (!subRepo.existsByUserIdAndTopicId(userId, topicId)) {
            subRepo.save(TopicSubscription(userId = userId, topicId = topicId))
        }
    }

    @Transactional
    fun unsubscribe(userId: Long, topicId: Long) {
        subRepo.deleteByUserIdAndTopicId(userId, topicId)
    }

    fun isSubscribed(userId: Long, topicId: Long): Boolean {
        return subRepo.existsByUserIdAndTopicId(userId, topicId)
    }
}
