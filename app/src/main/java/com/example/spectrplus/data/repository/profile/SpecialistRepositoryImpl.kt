package com.example.spectrplus.data.repository.profile

import com.example.spectrplus.core.network.absoluteUrl
import com.example.spectrplus.data.api.education.FileUploadApi
import com.example.spectrplus.data.api.profile.SpecialistApi
import com.example.spectrplus.data.dto.specialist.CreateSpecialistArticleBody
import com.example.spectrplus.data.dto.specialist.CreateSpecialistMaterialBody
import com.example.spectrplus.data.dto.specialist.CreateSpecialistVideoBody
import com.example.spectrplus.domain.repository.profile.SpecialistRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class SpecialistRepositoryImpl @Inject constructor(
    private val specialistApi: SpecialistApi,
    private val fileUploadApi: FileUploadApi
) : SpecialistRepository {

    override suspend fun uploadFile(
        folder: String,
        fileName: String,
        mimeType: String,
        bytes: ByteArray
    ): String {
        val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("file", fileName, body)
        val map = fileUploadApi.upload(part, folder)
        val url = map["url"] ?: throw IllegalStateException("Нет url в ответе")
        return absoluteUrl(url) ?: url
    }

    override suspend fun createArticle(
        title: String,
        content: String,
        category: String,
        sourceAttribution: String?,
        coverImageUrl: String?
    ) {
        specialistApi.createArticle(
            CreateSpecialistArticleBody(
                title = title,
                content = content,
                category = category,
                sourceAttribution = sourceAttribution?.takeIf { it.isNotBlank() },
                coverImageUrl = coverImageUrl?.takeIf { it.isNotBlank() }
            )
        )
    }

    override suspend fun createVideo(
        title: String,
        description: String,
        videoUrl: String,
        thumbnailUrl: String,
        durationSeconds: Int,
        category: String,
        sourceAttribution: String?
    ) {
        specialistApi.createVideo(
            CreateSpecialistVideoBody(
                title = title,
                description = description,
                videoUrl = videoUrl,
                thumbnailUrl = thumbnailUrl,
                durationSeconds = durationSeconds,
                category = category,
                sourceAttribution = sourceAttribution?.takeIf { it.isNotBlank() }
            )
        )
    }

    override suspend fun createMaterial(
        title: String,
        description: String,
        fileUrl: String,
        type: String,
        category: String,
        fileSize: Long,
        sourceAttribution: String?
    ) {
        specialistApi.createMaterial(
            CreateSpecialistMaterialBody(
                title = title,
                description = description,
                fileUrl = fileUrl,
                type = type,
                category = category,
                fileSize = fileSize,
                sourceAttribution = sourceAttribution?.takeIf { it.isNotBlank() }
            )
        )
    }
}
