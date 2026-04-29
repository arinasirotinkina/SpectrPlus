package com.example.spectrplus.dto.profile

data class PublicTherapyBriefDto(
    val title: String,
    val description: String
)

data class PublicChildProfileDto(
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val therapies: List<PublicTherapyBriefDto>
)

data class PublicUserResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String?,
    val city: String?,
    val accountRole: String,
    val specialistProfession: String?,
    val specialistEducation: String?,
    val specialistExperienceYears: Int?,
    val topicsCount: Int,
    val postsCount: Int,
    val showChildInPublicProfile: Boolean,
    val publicChildProfile: PublicChildProfileDto?
)
