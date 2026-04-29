package com.example.spectrplus.domain.repository.profile

import com.example.spectrplus.domain.model.profile.Profile
import com.example.spectrplus.domain.model.profile.UpdateProfileRequest
import java.io.InputStream

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(request: UpdateProfileRequest)
    suspend fun uploadAvatar(fileName: String, mimeType: String, inputStream: InputStream): String
    suspend fun deleteAvatar()
}
