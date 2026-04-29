package com.example.spectrplus.presentation.state

data class AuthState(
    val email: String = "",
    val password: String = "",
    val error: String? = null,
    val isLoading: Boolean = false
)