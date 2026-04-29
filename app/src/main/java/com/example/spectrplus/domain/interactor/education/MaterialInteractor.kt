package com.example.spectrplus.domain.interactor.education

import com.example.spectrplus.domain.model.education.Material
import com.example.spectrplus.domain.repository.education.MaterialRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MaterialInteractor @Inject constructor(
    private val repository: MaterialRepository
) {
    suspend fun getMaterials(category: String?): List<Material> =
        repository.getMaterials(category)
}
