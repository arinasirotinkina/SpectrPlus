package com.example.spectrplus.domain.model.profile

data class Child(
    val id: Long,
    val name: String,
    val age: Int,
    val gender: String,
    val diagnosis: String,
    val features: String,
    val notes: String,
    val therapies: List<Therapy>
)