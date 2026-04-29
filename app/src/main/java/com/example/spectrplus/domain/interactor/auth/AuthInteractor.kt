package com.example.spectrplus.domain.interactor.auth

import com.example.spectrplus.domain.model.auth.AuthResult
import com.example.spectrplus.domain.repository.auth.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInteractor @Inject constructor(
    private val repository: AuthRepository
) {
    suspend fun login(email: String, password: String): AuthResult =
        repository.login(email, password)

    suspend fun register(
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        password: String,
        asSpecialist: Boolean,
        specialistProfession: String?
    ): AuthResult = repository.register(
        email, phone, firstName, lastName, password, asSpecialist, specialistProfession
    )
}
