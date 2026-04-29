package com.example.spectrplus.data.repository.auth

import com.example.spectrplus.data.api.auth.AuthApi
import com.example.spectrplus.data.dto.auth.LoginRequest
import com.example.spectrplus.data.dto.auth.RegisterRequest
import com.example.spectrplus.domain.model.auth.AuthResult
import com.example.spectrplus.domain.repository.auth.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): AuthResult {

        val response = api.login(
            LoginRequest(email, password)
        )

        return AuthResult(
            token = response.token,
            accountRole = response.accountRole ?: "PARENT"
        )
    }

    override suspend fun register(
        email: String,
        phone: String,
        firstName: String,
        lastName: String,
        password: String,
        asSpecialist: Boolean,
        specialistProfession: String?
    ): AuthResult {

        val response = api.register(
            RegisterRequest(
                email = email,
                phone = phone,
                firstName = firstName,
                lastName = lastName,
                password = password,
                asSpecialist = asSpecialist,
                specialistProfession = specialistProfession
            )
        )

        return AuthResult(
            token = response.token,
            accountRole = response.accountRole ?: if (asSpecialist) "SPECIALIST" else "PARENT"
        )
    }

}