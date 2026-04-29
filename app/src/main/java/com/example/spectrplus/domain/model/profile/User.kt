package com.example.spectrplus.domain.model.profile

data class User(
    val id: Long,
    val email: String,
    val phone: String,
    val firstName: String,
    val lastName: String
)