package com.example.spectrplus.controller.profile

import com.example.spectrplus.dto.profile.plans.CreatePlanRequest
import com.example.spectrplus.dto.profile.plans.PlanResponse
import com.example.spectrplus.dto.profile.plans.UpdatePlanRequest
import com.example.spectrplus.service.profile.PlanService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/plans")
class PlanController(
    private val service: PlanService
) {

    @GetMapping
    fun getPlans(
        authentication: Authentication,
        @RequestParam(required = false) month: String?
    ): List<PlanResponse> {
        return service.getPlans(authentication.name, month)
    }

    @PostMapping
    fun addPlan(
        authentication: Authentication,
        @RequestBody req: CreatePlanRequest
    ): PlanResponse {
        return service.addPlan(authentication.name, req)
    }

    @PutMapping("/{id}")
    fun updatePlan(
        authentication: Authentication,
        @PathVariable id: Long,
        @RequestBody req: UpdatePlanRequest
    ): PlanResponse {
        return service.updatePlan(authentication.name, id, req)
    }

    @PostMapping("/{id}/toggle")
    fun toggleDone(
        authentication: Authentication,
        @PathVariable id: Long
    ): PlanResponse {
        return service.toggleDone(authentication.name, id)
    }

    @DeleteMapping("/{id}")
    fun deletePlan(
        authentication: Authentication,
        @PathVariable id: Long
    ) {
        service.deletePlan(authentication.name, id)
    }
}