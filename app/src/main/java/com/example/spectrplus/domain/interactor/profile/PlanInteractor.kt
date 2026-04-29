package com.example.spectrplus.domain.interactor.profile

import com.example.spectrplus.domain.model.profile.Plan
import com.example.spectrplus.domain.repository.profile.PlanRepository
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlanInteractor @Inject constructor(
    private val repository: PlanRepository
) {
    suspend fun getPlans(month: String?): List<Plan> = repository.getPlans(month)

    suspend fun addPlan(date: LocalDate, title: String, time: String): Plan =
        repository.addPlan(date, title, time)

    suspend fun updatePlan(
        id: Long,
        date: LocalDate,
        title: String,
        time: String,
        done: Boolean
    ): Plan = repository.updatePlan(id, date, title, time, done)

    suspend fun toggleDone(id: Long): Plan = repository.toggleDone(id)

    suspend fun deletePlan(id: Long) = repository.deletePlan(id)
}
