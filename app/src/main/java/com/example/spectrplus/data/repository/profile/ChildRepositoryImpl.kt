package com.example.spectrplus.data.repository.profile

import com.example.spectrplus.data.api.profile.ChildApi
import com.example.spectrplus.data.dto.profile.CreateChildRequestDto
import com.example.spectrplus.data.dto.profile.TherapyRequestDto
import com.example.spectrplus.data.dto.profile.UpdateChildRequestDto
import com.example.spectrplus.domain.model.profile.CreateChildRequest
import com.example.spectrplus.domain.model.profile.UpdateChildRequest
import com.example.spectrplus.domain.repository.profile.ChildRepository
import javax.inject.Inject

class ChildRepositoryImpl @Inject constructor(
    private val api: ChildApi
) : ChildRepository {

    override suspend fun addChild(request: CreateChildRequest) {
        api.addChild(
            CreateChildRequestDto(
                name = request.name,
                age = request.age,
                gender = request.gender,
                diagnosis = request.diagnosis,
                features = request.features,
                notes = request.notes,
                therapies = request.therapies.map {
                    TherapyRequestDto(it.title, it.description)
                }
            )
        )
    }

    override suspend fun updateChild(id: Long, request: UpdateChildRequest) {
        api.updateChild(
            id,
            UpdateChildRequestDto(
                name = request.name,
                age = request.age,
                gender = request.gender,
                diagnosis = request.diagnosis,
                features = request.features,
                notes = request.notes,
                therapies = request.therapies.map {
                    TherapyRequestDto(it.title, it.description)
                }
            )
        )
    }

    override suspend fun deleteChild(id: Long) {
        api.deleteChild(id)
    }
}
