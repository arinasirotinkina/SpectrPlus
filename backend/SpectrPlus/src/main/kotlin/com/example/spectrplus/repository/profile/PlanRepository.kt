package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.Plan
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface PlanRepository : JpaRepository<Plan, Long> {

    fun findByUserIdAndDateBetweenOrderByTimeAsc(
        userId: Long,
        from: LocalDate,
        to: LocalDate
    ): List<Plan>

    fun findByUserIdOrderByDateAscTimeAsc(userId: Long): List<Plan>
}
