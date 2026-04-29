package com.example.spectrplus.data.dto.profile

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
    val city: String? = null,
    val accountRole: String = "PARENT",
    val specialistProfession: String? = null,
    val specialistEducation: String? = null,
    val specialistExperienceYears: Int? = null,
    val topicsCount: Int,
    val postsCount: Int,
    val showChildInPublicProfile: Boolean = false,
    val publicChildProfile: PublicChildProfileDto? = null
)
