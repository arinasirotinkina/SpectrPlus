package com.example.spectrplus.domain.model.social

import com.example.spectrplus.domain.model.profile.Therapy

data class PublicChildProfile(
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val therapies: List<Therapy>
)
