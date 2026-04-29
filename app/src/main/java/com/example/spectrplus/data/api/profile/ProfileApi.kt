package com.example.spectrplus.data.api.profile

import com.example.spectrplus.data.dto.profile.ProfileResponseDto
import com.example.spectrplus.data.dto.profile.UpdateProfileRequestDto
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface ProfileApi {

    @GET("profile")
    suspend fun getProfile(): ProfileResponseDto

    @PUT("profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    )

    @Multipart
    @POST("profile/avatar")
    suspend fun uploadAvatar(
        @Part file: MultipartBody.Part
    ): Map<String, String>


    @DELETE("profile/avatar")
    suspend fun deleteAvatar()
}