package com.example.spectrplus.data.dto.profile

data class CreateChildRequestDto(
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,
    val therapies: List<TherapyRequestDto> = emptyList()
)
