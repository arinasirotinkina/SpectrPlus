package com.example.spectrplus.data.api.education

import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface FileUploadApi {
    @Multipart
    @POST("api/files/upload")
    suspend fun upload(
        @Part file: MultipartBody.Part,
        @Query("folder") folder: String
    ): Map<String, String>
}
