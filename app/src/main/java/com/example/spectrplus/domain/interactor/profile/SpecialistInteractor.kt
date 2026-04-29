package com.example.spectrplus.domain.interactor.profile

import com.example.spectrplus.domain.repository.profile.SpecialistRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpecialistInteractor @Inject constructor(
    private val repository: SpecialistRepository
) {
    suspend fun uploadFile(folder: String, fileName: String, mimeType: String, bytes: ByteArray): String =
        repository.uploadFile(folder, fileName, mimeType, bytes)

    suspend fun createArticle(
        title: String,
        content: String,
        category: String,
        sourceAttribution: String?,
        coverImageUrl: String?
    ) = repository.createArticle(title, content, category, sourceAttribution, coverImageUrl)

    suspend fun createVideo(
        title: String,
        description: String,
        videoUrl: String,
        thumbnailUrl: String,
        durationSeconds: Int,
        category: String,
        sourceAttribution: String?
    ) = repository.createVideo(
        title, description, videoUrl, thumbnailUrl, durationSeconds, category, sourceAttribution
    )

    suspend fun createMaterial(
        title: String,
        description: String,
        fileUrl: String,
        type: String,
        category: String,
        fileSize: Long,
        sourceAttribution: String?
    ) = repository.createMaterial(
        title, description, fileUrl, type, category, fileSize, sourceAttribution
    )
}
