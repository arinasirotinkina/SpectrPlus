package com.example.spectrplus.presentation.state

data class RegisterState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val specialistProfession: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
