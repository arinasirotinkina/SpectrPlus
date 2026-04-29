package com.example.spectrplus.service.profile

import com.example.spectrplus.dto.profile.plans.CreatePlanRequest
import com.example.spectrplus.dto.profile.plans.PlanResponse
import com.example.spectrplus.dto.profile.plans.UpdatePlanRequest
import com.example.spectrplus.entity.profile.Plan
import com.example.spectrplus.repository.profile.PlanRepository
import com.example.spectrplus.repository.profile.UserRepository
import org.springframework.stereotype.Service
import java.time.YearMonth

@Service
class PlanService(
    private val repository: PlanRepository,
    private val userRepository: UserRepository
) {

    fun getPlans(email: String, month: String?): List<PlanResponse> {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val plans = if (month != null) {
            val ym = YearMonth.parse(month)
            repository.findByUserIdAndDateBetweenOrderByTimeAsc(
                user.id,
                ym.atDay(1),
                ym.atEndOfMonth()
            )
        } else {
            repository.findByUserIdOrderByDateAscTimeAsc(user.id)
        }

        return plans.map { it.toDto() }
    }

    fun addPlan(email: String, req: CreatePlanRequest): PlanResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val plan = repository.save(
            Plan(
                userId = user.id,
                date = req.date,
                title = req.title,
                time = req.time
            )
        )

        return plan.toDto()
    }

    fun updatePlan(email: String, id: Long, req: UpdatePlanRequest): PlanResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val plan = repository.findById(id)
            .orElseThrow { RuntimeException("Plan not found") }

        if (plan.userId != user.id) throw RuntimeException("Forbidden")

        plan.date = req.date
        plan.title = req.title
        plan.time = req.time
        plan.done = req.done

        return repository.save(plan).toDto()
    }

    fun toggleDone(email: String, id: Long): PlanResponse {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val plan = repository.findById(id)
            .orElseThrow { RuntimeException("Plan not found") }

        if (plan.userId != user.id) throw RuntimeException("Forbidden")

        plan.done = !plan.done

        return repository.save(plan).toDto()
    }

    fun deletePlan(email: String, id: Long) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val plan = repository.findById(id)
            .orElseThrow { RuntimeException("Plan not found") }

        if (plan.userId != user.id) throw RuntimeException("Forbidden")

        repository.deleteById(id)
    }

    private fun Plan.toDto() = PlanResponse(
        id = id,
        date = date,
        title = title,
        time = time,
        done = done
    )
}
