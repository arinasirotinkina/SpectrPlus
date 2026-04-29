package com.example.spectrplus.data.repository.education

import com.example.spectrplus.data.api.education.MaterialApi
import com.example.spectrplus.data.mapper.toDomain
import com.example.spectrplus.domain.model.education.Material
import com.example.spectrplus.domain.repository.education.MaterialRepository
import javax.inject.Inject

class MaterialRepositoryImpl @Inject constructor(
    private val api: MaterialApi
) : MaterialRepository {

    override suspend fun getMaterials(category: String?): List<Material> {
        return api.getMaterials(category).map { it.toDomain() }
    }
}