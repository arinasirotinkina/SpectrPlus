package com.example.spectrplus.service.auth

import com.example.spectrplus.dto.auth.AuthResponse
import com.example.spectrplus.dto.auth.LoginRequest
import com.example.spectrplus.dto.auth.RegisterRequest
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.entity.profile.User
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.security.JwtService
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    fun register(request: RegisterRequest): AuthResponse {

        if (request.email.isBlank() || request.password.isBlank() ||
            request.firstName.isBlank() || request.lastName.isBlank()
        ) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Заполните все обязательные поля")
        }

        if (request.asSpecialist && request.specialistProfession.isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Укажите профиль специалиста (например, психолог)")
        }

        if (userRepository.existsByEmail(request.email)) {
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Пользователь с таким email уже зарегистрирован"
            )
        }

        val role = if (request.asSpecialist) AccountRole.SPECIALIST else AccountRole.PARENT
        val user = User(
            email = request.email,
            phone = request.phone,
            firstName = request.firstName,
            lastName = request.lastName,
            password = passwordEncoder.encode(request.password),
            accountRole = role,
            specialistProfession = if (request.asSpecialist) request.specialistProfession?.trim() else null
        )

        val saved = userRepository.save(user)
        val token = jwtService.generateToken(saved)
        return AuthResponse(token = token, accountRole = saved.accountRole.name)
    }

    fun login(request: LoginRequest): AuthResponse {

        val user = userRepository.findByEmail(request.email)
            ?: throw ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Пользователь с таким email не найден"
            )

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Неверный пароль")
        }

        val token = jwtService.generateToken(user)

        return AuthResponse(token = token, accountRole = user.accountRole.name)
    }

}