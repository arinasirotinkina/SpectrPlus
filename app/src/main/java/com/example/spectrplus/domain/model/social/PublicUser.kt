package com.example.spectrplus.domain.model.social

data class PublicUser(
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
    val publicChildProfile: PublicChildProfile?
)
