package com.example.spectrplus.data.api.profile

import com.example.spectrplus.data.dto.profile.CreatePlanRequestDto
import com.example.spectrplus.data.dto.profile.PlanResponseDto
import com.example.spectrplus.data.dto.profile.UpdatePlanRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface PlanApi {

    @GET("plans")
    suspend fun getPlans(
        @Query("month") month: String?
    ): List<PlanResponseDto>

    @POST("plans")
    suspend fun addPlan(
        @Body request: CreatePlanRequestDto
    ): PlanResponseDto

    @PUT("plans/{id}")
    suspend fun updatePlan(
        @Path("id") id: Long,
        @Body request: UpdatePlanRequestDto
    ): PlanResponseDto

    @POST("plans/{id}/toggle")
    suspend fun toggleDone(@Path("id") id: Long): PlanResponseDto

    @DELETE("plans/{id}")
    suspend fun deletePlan(@Path("id") id: Long)
}
