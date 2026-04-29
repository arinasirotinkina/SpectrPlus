package com.example.spectrplus.data.dto.auth

data class LoginResponse(
    val token: String,
    val accountRole: String? = null
)