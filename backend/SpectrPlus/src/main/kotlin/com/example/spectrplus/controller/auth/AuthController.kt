package com.example.spectrplus.controller.auth

import com.example.spectrplus.dto.auth.AuthResponse
import com.example.spectrplus.dto.auth.LoginRequest
import com.example.spectrplus.dto.auth.RegisterRequest
import com.example.spectrplus.service.auth.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(
        @RequestBody request: RegisterRequest
    ): AuthResponse {
        return authService.register(request)
    }

    @PostMapping("/login")
    fun login(
        @RequestBody request: LoginRequest
    ): AuthResponse {
        return authService.login(request)
    }
}