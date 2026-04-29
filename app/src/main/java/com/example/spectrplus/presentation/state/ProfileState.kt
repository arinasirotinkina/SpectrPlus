package com.example.spectrplus.presentation.state

import com.example.spectrplus.domain.model.profile.Profile

data class ProfileState(
    val profile: Profile? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)