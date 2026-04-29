package com.example.spectrplus.data.api.education

import com.example.spectrplus.data.dto.articles.MaterialDto
import retrofit2.http.GET
import retrofit2.http.Query

interface MaterialApi {

    @GET("materials")
    suspend fun getMaterials(
        @Query("category") category: String?
    ): List<MaterialDto>
}