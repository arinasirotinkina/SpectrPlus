package com.example.spectrplus.domain.model.profile

data class CreateChildRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,
    val therapies: List<TherapyRequest> = emptyList()
)
