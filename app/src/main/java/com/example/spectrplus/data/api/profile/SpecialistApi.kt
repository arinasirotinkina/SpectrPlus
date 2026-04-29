package com.example.spectrplus.data.api.profile

import com.example.spectrplus.data.dto.articles.ArticleDto
import com.example.spectrplus.data.dto.articles.MaterialDto
import com.example.spectrplus.data.dto.articles.VideoDto
import com.example.spectrplus.data.dto.specialist.CreateSpecialistArticleBody
import com.example.spectrplus.data.dto.specialist.CreateSpecialistMaterialBody
import com.example.spectrplus.data.dto.specialist.CreateSpecialistVideoBody
import retrofit2.http.Body
import retrofit2.http.POST

interface SpecialistApi {

    @POST("api/specialist/content/articles")
    suspend fun createArticle(@Body body: CreateSpecialistArticleBody): ArticleDto

    @POST("api/specialist/content/videos")
    suspend fun createVideo(@Body body: CreateSpecialistVideoBody): VideoDto

    @POST("api/specialist/content/materials")
    suspend fun createMaterial(@Body body: CreateSpecialistMaterialBody): MaterialDto
}
