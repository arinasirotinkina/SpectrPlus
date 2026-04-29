package com.example.spectrplus.domain.interactor.profile

import com.example.spectrplus.domain.model.profile.CreateChildRequest
import com.example.spectrplus.domain.model.profile.UpdateChildRequest
import com.example.spectrplus.domain.repository.profile.ChildRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildInteractor @Inject constructor(
    private val repository: ChildRepository
) {
    suspend fun addChild(request: CreateChildRequest) = repository.addChild(request)

    suspend fun updateChild(id: Long, request: UpdateChildRequest) =
        repository.updateChild(id, request)

    suspend fun deleteChild(id: Long) = repository.deleteChild(id)
}
