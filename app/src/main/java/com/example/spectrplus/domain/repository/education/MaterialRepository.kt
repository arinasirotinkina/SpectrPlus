package com.example.spectrplus.domain.repository.education

import com.example.spectrplus.domain.model.education.Material

interface MaterialRepository {

    suspend fun getMaterials(category: String? = null): List<Material>
}