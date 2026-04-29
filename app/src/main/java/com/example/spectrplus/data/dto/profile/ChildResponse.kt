package com.example.spectrplus.data.dto.profile

class ChildResponse {
}
data class ProfileResponseDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val city: String? = null,
    val avatarUrl: String?,
    val accountRole: String = "PARENT",
    val showChildInPublicProfile: Boolean = false,
    val specialistProfession: String? = null,
    val specialistEducation: String? = null,
    val specialistExperienceYears: Int? = null,
    val children: List<ChildDto>
)

data class ChildDto(
    val id: Long,
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,
    val therapies: List<TherapyDto>
)

data class TherapyDto(
    val title: String,
    val description: String
)

data class UpdateProfileRequestDto(
    val firstName: String,
    val lastName: String,
    val phone: String,
    val city: String?,
    val showChildInPublicProfile: Boolean,
    val specialistProfession: String?,
    val specialistEducation: String?,
    val specialistExperienceYears: Int?
)