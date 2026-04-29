package com.example.spectrplus.domain.interactor.profile

import com.example.spectrplus.domain.model.profile.Profile
import com.example.spectrplus.domain.model.profile.UpdateProfileRequest
import com.example.spectrplus.domain.repository.profile.ProfileRepository
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileInteractor @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend fun getProfile(): Profile = repository.getProfile()

    suspend fun updateProfile(request: UpdateProfileRequest) =
        repository.updateProfile(request)

    suspend fun uploadAvatar(fileName: String, mimeType: String, inputStream: InputStream): String =
        repository.uploadAvatar(fileName, mimeType, inputStream)

    suspend fun deleteAvatar() = repository.deleteAvatar()
}
