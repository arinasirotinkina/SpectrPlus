package com.example.spectrplus.domain.repository.profile

interface SpecialistRepository {
    suspend fun uploadFile(folder: String, fileName: String, mimeType: String, bytes: ByteArray): String

    suspend fun createArticle(
        title: String,
        content: String,
        category: String,
        sourceAttribution: String?,
        coverImageUrl: String?
    )

    suspend fun createVideo(
        title: String,
        description: String,
        videoUrl: String,
        thumbnailUrl: String,
        durationSeconds: Int,
        category: String,
        sourceAttribution: String?
    )

    suspend fun createMaterial(
        title: String,
        description: String,
        fileUrl: String,
        type: String,
        category: String,
        fileSize: Long,
        sourceAttribution: String?
    )
}
