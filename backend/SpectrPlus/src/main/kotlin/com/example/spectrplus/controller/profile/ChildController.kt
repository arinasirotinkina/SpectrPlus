package com.example.spectrplus.controller.profile

import com.example.spectrplus.dto.profile.child.CreateChildRequest
import com.example.spectrplus.dto.profile.UpdateChildRequest
import com.example.spectrplus.service.profile.ChildService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/children")
class ChildController(
    private val childService: ChildService
) {

    @PostMapping
    fun addChild(
        authentication: Authentication,
        @RequestBody request: CreateChildRequest
    ) {
        childService.addChild(authentication.name, request)
    }

    @PutMapping("/{id}")
    fun updateChild(
        @PathVariable id: Long,
        @RequestBody request: UpdateChildRequest
    ) {
        childService.updateChild(id, request)
    }

    @DeleteMapping("/{id}")
    fun deleteChild(
        @PathVariable id: Long
    ) {
        childService.deleteChild(id)
    }
}