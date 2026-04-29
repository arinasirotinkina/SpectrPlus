package com.example.spectrplus.dto.profile.child

import com.example.spectrplus.dto.profile.TherapyResponse

data class ChildResponse(

    val id: Long,
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,

    val therapies: List<TherapyResponse>
)