package com.example.spectrplus.controller.social

import com.example.spectrplus.dto.social.CreatePostRequest
import com.example.spectrplus.dto.social.CreateTopicRequest
import com.example.spectrplus.dto.social.PostResponse
import com.example.spectrplus.dto.social.TopicResponse
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.service.social.ForumService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/forum")
class ForumController(
    private val service: ForumService,
    private val userRepository: UserRepository
) {

    private fun getUser(authentication: Authentication) =
        userRepository.findByEmail(authentication.name)
            ?: throw RuntimeException("User not found")

    @GetMapping("/topics")
    fun getTopics(
        @RequestParam(required = false) category: String?
    ): List<TopicResponse> {
        return service.getTopics(category)
    }

    @GetMapping("/topics/{id}")
    fun getTopic(
        @PathVariable id: Long
    ): TopicResponse {
        return service.getTopic(id)
    }

    @PostMapping("/topics")
    fun createTopic(
        authentication: Authentication,
        @RequestBody req: CreateTopicRequest
    ): TopicResponse {

        val user = getUser(authentication)

        return service.createTopic(
            userId = user.id,
            userName = "${user.firstName} ${user.lastName}",
            req = req
        )
    }

    @GetMapping("/topics/{id}/posts")
    fun getPosts(
        @PathVariable id: Long
    ): List<PostResponse> {
        return service.getPosts(id)
    }

    @PostMapping("/topics/{id}/posts")
    fun addPost(
        authentication: Authentication,
        @PathVariable id: Long,
        @RequestBody req: CreatePostRequest
    ): PostResponse {

        val user = getUser(authentication)

        return service.addPost(
            userId = user.id,
            userName = "${user.firstName} ${user.lastName}",
            topicId = id,
            req = req
        )
    }

    @PutMapping("/posts/{id}")
    fun editPost(
        authentication: Authentication,
        @PathVariable id: Long,
        @RequestBody req: CreatePostRequest
    ): PostResponse {
        val user = getUser(authentication)
        return service.editPost(user.id, id, req.content)
    }

    @DeleteMapping("/posts/{id}")
    fun deletePost(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        val user = getUser(authentication)
        service.deletePost(user.id, id)
    }

    @PostMapping("/topics/{id}/subscribe")
    fun subscribe(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        val user = getUser(authentication)
        service.subscribe(userId = user.id, topicId = id)
    }

    @DeleteMapping("/topics/{id}/subscribe")
    fun unsubscribe(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        val user = getUser(authentication)
        service.unsubscribe(userId = user.id, topicId = id)
    }

    @GetMapping("/topics/{id}/subscribed")
    fun isSubscribed(
        authentication: Authentication,
        @PathVariable id: Long
    ): Map<String, Boolean> {
        val user = getUser(authentication)
        return mapOf("subscribed" to service.isSubscribed(user.id, id))
    }
}