package com.example.spectrplus.domain.repository.social

import com.example.spectrplus.domain.model.social.PublicUser

interface UserRepository {
    suspend fun getPublicUser(id: Long): PublicUser
}
