package com.example.spectrplus.domain.model.profile

data class Profile(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val city: String?,
    val avatarUrl: String?,
    val accountRole: String,
    val showChildInPublicProfile: Boolean,
    val specialistProfession: String?,
    val specialistEducation: String?,
    val specialistExperienceYears: Int?,
    val children: List<Child>
)