package com.example.spectrplus.domain.repository.profile

import com.example.spectrplus.domain.model.profile.Plan
import java.time.LocalDate

interface PlanRepository {
    suspend fun getPlans(month: String?): List<Plan>
    suspend fun addPlan(date: LocalDate, title: String, time: String): Plan
    suspend fun updatePlan(id: Long, date: LocalDate, title: String, time: String, done: Boolean): Plan
    suspend fun toggleDone(id: Long): Plan
    suspend fun deletePlan(id: Long)
}
