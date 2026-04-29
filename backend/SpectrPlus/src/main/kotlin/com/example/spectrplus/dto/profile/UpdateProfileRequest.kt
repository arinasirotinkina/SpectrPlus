package com.example.spectrplus.dto.profile

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val city: String?,
    val showChildInPublicProfile: Boolean = false,
    val specialistProfession: String? = null,
    val specialistEducation: String? = null,
    val specialistExperienceYears: Int? = null
)