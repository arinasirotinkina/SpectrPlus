package com.example.spectrplus.domain.model.auth

data class AuthResult(
    val token: String,
    val accountRole: String
)
