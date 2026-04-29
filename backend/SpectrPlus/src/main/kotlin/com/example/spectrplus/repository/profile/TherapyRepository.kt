package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.Therapy
import org.springframework.data.jpa.repository.JpaRepository

interface TherapyRepository : JpaRepository<Therapy, Long> {
    fun findByChildId(childId: Long): List<Therapy>
    fun deleteByChildId(childId: Long)
}
