package com.example.spectrplus.repository.education

import com.example.spectrplus.entity.education.Material
import com.example.spectrplus.entity.education.MaterialCategory
import org.springframework.data.jpa.repository.JpaRepository

interface MaterialRepository : JpaRepository<Material, Long> {

    fun findByCategory(category: MaterialCategory): List<Material>

    fun findByTitleContainingIgnoreCase(query: String): List<Material>
}