package com.example.spectrplus.dto.auth

data class AuthResponse(
    val token: String,
    val accountRole: String
)