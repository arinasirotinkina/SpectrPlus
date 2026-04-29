package com.example.spectrplus.data.api.auth

import com.example.spectrplus.data.dto.auth.LoginRequest
import com.example.spectrplus.data.dto.auth.LoginResponse
import com.example.spectrplus.data.dto.auth.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse


    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): LoginResponse
}