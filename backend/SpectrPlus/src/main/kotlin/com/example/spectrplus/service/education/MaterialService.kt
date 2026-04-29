package com.example.spectrplus.service.education

import com.example.spectrplus.dto.education.material.MaterialResponse
import com.example.spectrplus.entity.education.Material
import com.example.spectrplus.entity.education.MaterialCategory
import com.example.spectrplus.entity.education.MaterialType
import com.example.spectrplus.entity.profile.AccountRole
import com.example.spectrplus.mapper.toDto
import com.example.spectrplus.repository.education.MaterialRepository
import com.example.spectrplus.repository.profile.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException


@Service
class MaterialService(
    private val repository: MaterialRepository,
    private val userRepository: UserRepository
) {

    fun getAll(category: String?, query: String?): List<MaterialResponse> {

        val materials = when {
            !query.isNullOrBlank() ->
                repository.findByTitleContainingIgnoreCase(query)

            !category.isNullOrBlank() ->
                repository.findByCategory(MaterialCategory.valueOf(category))

            else -> repository.findAll()
        }

        return materials.map { it.toDto() }
    }

    fun getById(id: Long): MaterialResponse {
        val material = repository.findById(id)
            .orElseThrow { RuntimeException("Material not found") }

        return material.toDto()
    }

    fun createBySpecialist(
        email: String,
        title: String,
        description: String,
        fileUrl: String,
        type: String,
        category: String,
        fileSize: Long,
        sourceAttribution: String?
    ): MaterialResponse {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        if (user.accountRole != AccountRole.SPECIALIST) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "Только для специалистов")
        }
        val t = try {
            MaterialType.valueOf(type)
        } catch (_: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестный тип файла")
        }
        val c = try {
            MaterialCategory.valueOf(category)
        } catch (_: Exception) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Неизвестная категория")
        }
        val saved = repository.save(
            Material(
                title = title.trim(),
                description = description.trim(),
                fileUrl = fileUrl.trim(),
                type = t,
                category = c,
                fileSize = fileSize,
                sourceAttribution = sourceAttribution?.trim()?.takeIf { it.isNotEmpty() }
            )
        )
        return saved.toDto()
    }
}