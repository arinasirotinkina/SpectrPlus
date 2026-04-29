package com.example.spectrplus.domain.interactor.profile

import com.example.spectrplus.domain.model.social.PublicUser
import com.example.spectrplus.domain.repository.UserRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserInteractor @Inject constructor(
    private val repository: UserRepository
) {
    suspend fun getPublicUser(id: Long): PublicUser = repository.getPublicUser(id)
}
