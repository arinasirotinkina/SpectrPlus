package com.example.spectrplus.data.repository.profile

import com.example.spectrplus.data.api.profile.ProfileApi
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.data.mapper.toDto
import com.example.spectrplus.domain.model.profile.Profile
import com.example.spectrplus.domain.model.profile.UpdateProfileRequest
import com.example.spectrplus.domain.repository.profile.ProfileRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.InputStream
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: ProfileApi
) : ProfileRepository {

    override suspend fun getProfile(): Profile {
        return api.getProfile().toDomain()
    }

    override suspend fun updateProfile(request: UpdateProfileRequest) {
        api.updateProfile(request.toDto())
    }

    override suspend fun uploadAvatar(
        fileName: String,
        mimeType: String,
        inputStream: InputStream
    ): String {

        val bytes = inputStream.use { it.readBytes() }

        val requestBody = bytes.toRequestBody(mimeType.toMediaTypeOrNull())

        val part = MultipartBody.Part.createFormData(
            "file",
            fileName,
            requestBody
        )

        val requestBody1 = bytes.toRequestBody("image/jpeg".toMediaType())

        val part1 = MultipartBody.Part.createFormData(
            "file",
            "avatar.jpg",
            requestBody1
        )

            //api.uploadAvatar(part1)

        val result = api.uploadAvatar(part)

        return result["avatarUrl"] ?: ""
    }

    override suspend fun deleteAvatar() {
        api.deleteAvatar()
    }
}