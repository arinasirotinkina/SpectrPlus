package com.example.spectrplus.presentation.state

data class EditProfileState(
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val city: String = "",
    val showChildInPublicProfile: Boolean = false,
    val specialistProfession: String = "",
    val specialistEducation: String = "",
    val specialistExperienceYears: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
