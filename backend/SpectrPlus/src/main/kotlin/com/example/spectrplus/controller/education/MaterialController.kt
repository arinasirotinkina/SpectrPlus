package com.example.spectrplus.controller.education

import com.example.spectrplus.dto.education.material.MaterialResponse
import com.example.spectrplus.service.education.MaterialService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/materials")
class MaterialController(
    private val service: MaterialService
) {

    @GetMapping
    fun getMaterials(
        @RequestParam(required = false) category: String?,
        @RequestParam(required = false) query: String?
    ): List<MaterialResponse> {
        return service.getAll(category, query)
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): MaterialResponse {
        return service.getById(id)
    }
}