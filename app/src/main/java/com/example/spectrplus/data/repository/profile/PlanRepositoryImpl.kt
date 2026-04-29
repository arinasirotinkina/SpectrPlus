package com.example.spectrplus.data.repository.profile

import com.example.spectrplus.data.api.profile.PlanApi
import com.example.spectrplus.data.dto.profile.CreatePlanRequestDto
import com.example.spectrplus.data.dto.profile.PlanResponseDto
import com.example.spectrplus.data.dto.profile.UpdatePlanRequestDto
import com.example.spectrplus.domain.model.profile.Plan
import com.example.spectrplus.domain.repository.profile.PlanRepository
import java.time.LocalDate
import javax.inject.Inject

class PlanRepositoryImpl @Inject constructor(
    private val api: PlanApi
) : PlanRepository {

    private fun PlanResponseDto.toDomain(): Plan = Plan(
        id = id,
        date = LocalDate.parse(date),
        title = title,
        time = time,
        done = done
    )

    override suspend fun getPlans(month: String?): List<Plan> {
        return api.getPlans(month).map { it.toDomain() }
    }

    override suspend fun addPlan(date: LocalDate, title: String, time: String): Plan {
        return api.addPlan(
            CreatePlanRequestDto(date.toString(), title, time)
        ).toDomain()
    }

    override suspend fun updatePlan(
        id: Long,
        date: LocalDate,
        title: String,
        time: String,
        done: Boolean
    ): Plan {
        return api.updatePlan(
            id,
            UpdatePlanRequestDto(date.toString(), title, time, done)
        ).toDomain()
    }

    override suspend fun toggleDone(id: Long): Plan {
        return api.toggleDone(id).toDomain()
    }

    override suspend fun deletePlan(id: Long) {
        api.deletePlan(id)
    }
}
