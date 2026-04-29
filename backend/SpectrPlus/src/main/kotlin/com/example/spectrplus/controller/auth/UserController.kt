package com.example.spectrplus.controller.auth

import com.example.spectrplus.dto.profile.PublicChildProfileDto
import com.example.spectrplus.dto.profile.PublicTherapyBriefDto
import com.example.spectrplus.dto.profile.PublicUserResponse
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.repository.profile.ChildRepository
import com.example.spectrplus.repository.profile.TherapyRepository
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.repository.social.PostRepository
import com.example.spectrplus.repository.social.TopicRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val userRepository: UserRepository,
    private val childRepository: ChildRepository,
    private val therapyRepository: TherapyRepository,
    private val topicRepository: TopicRepository,
    private val postRepository: PostRepository
) {

    @GetMapping("/{id}")
    fun getById(
        @PathVariable id: Long
    ): PublicUserResponse {

        val user = userRepository.findById(id)
            .orElseThrow { RuntimeException("User not found") }

        val topicsCount = topicRepository.countByAuthorId(user.id).toInt()
        val postsCount = postRepository.countByAuthorId(user.id).toInt()

        val publicChild = if (
            user.accountRole == AccountRole.PARENT &&
            user.showChildInPublicProfile
        ) {
            val children = childRepository.findByUserId(user.id)
            val first = children.firstOrNull()
            if (first != null) {
                val therapies = therapyRepository.findByChildId(first.id).map {
                    PublicTherapyBriefDto(it.title, it.description)
                }
                PublicChildProfileDto(
                    name = first.name,
                    age = first.age,
                    gender = first.gender,
                    diagnosis = first.diagnosis,
                    therapies = therapies
                )
            } else null
        } else null

        return PublicUserResponse(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            avatarUrl = user.avatarUrl,
            city = user.city,
            accountRole = user.accountRole.name,
            specialistProfession = user.specialistProfession,
            specialistEducation = user.specialistEducation,
            specialistExperienceYears = user.specialistExperienceYears,
            topicsCount = topicsCount,
            postsCount = postsCount,
            showChildInPublicProfile = user.showChildInPublicProfile,
            publicChildProfile = publicChild
        )
    }
}