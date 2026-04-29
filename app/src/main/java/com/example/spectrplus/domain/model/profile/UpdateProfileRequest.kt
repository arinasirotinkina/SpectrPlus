package com.example.spectrplus.domain.model.profile

data class UpdateProfileRequest(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val city: String?,
    val showChildInPublicProfile: Boolean,
    val specialistProfession: String?,
    val specialistEducation: String?,
    val specialistExperienceYears: Int?
)