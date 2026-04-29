package com.example.spectrplus.domain.repository.auth

import com.example.spectrplus.domain.model.auth.AuthResult

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String
    ): AuthResult

    suspend fun register(
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        password: String,
        asSpecialist: Boolean,
        specialistProfession: String?
    ): AuthResult
}