package com.example.spectrplus.data.api.profile

import com.example.spectrplus.data.dto.profile.CreateChildRequestDto
import com.example.spectrplus.data.dto.profile.UpdateChildRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ChildApi {

    @POST("children")
    suspend fun addChild(@Body request: CreateChildRequestDto)

    @PUT("children/{id}")
    suspend fun updateChild(
        @Path("id") id: Long,
        @Body request: UpdateChildRequestDto
    )

    @DELETE("children/{id}")
    suspend fun deleteChild(@Path("id") id: Long)
}
