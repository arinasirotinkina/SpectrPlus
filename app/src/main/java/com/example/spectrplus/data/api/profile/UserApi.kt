package com.example.spectrplus.data.api.profile

import com.example.spectrplus.data.dto.profile.PublicUserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {
    @GET("users/{id}")
    suspend fun getById(@Path("id") id: Long): PublicUserResponse
}
