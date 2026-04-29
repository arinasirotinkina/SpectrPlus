package com.example.spectrplus.domain.repository.profile

import com.example.spectrplus.domain.model.profile.CreateChildRequest
import com.example.spectrplus.domain.model.profile.UpdateChildRequest

interface ChildRepository {
    suspend fun addChild(request: CreateChildRequest)
    suspend fun updateChild(id: Long, request: UpdateChildRequest)
    suspend fun deleteChild(id: Long)
}
