package com.example.spectrplus.dto.auth

data class RegisterRequest(

    val email: String,
    val phone: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val asSpecialist: Boolean = false,
    val specialistProfession: String? = null
)