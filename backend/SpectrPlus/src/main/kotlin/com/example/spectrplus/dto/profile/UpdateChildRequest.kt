package com.example.spectrplus.dto.profile

import com.example.spectrplus.dto.profile.child.TherapyRequest

data class UpdateChildRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,
    val therapies: List<TherapyRequest> = emptyList()
)
