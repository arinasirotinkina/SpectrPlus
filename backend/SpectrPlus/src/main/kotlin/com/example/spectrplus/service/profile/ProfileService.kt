package com.example.spectrplus.service.profile

import com.example.spectrplus.dto.profile.ProfileResponse
import com.example.spectrplus.dto.profile.TherapyResponse
import com.example.spectrplus.dto.profile.UpdateProfileRequest
import com.example.spectrplus.dto.profile.child.ChildResponse
import com.example.spectrplus.repository.profile.ChildRepository
import com.example.spectrplus.repository.profile.TherapyRepository
import com.example.spectrplus.repository.profile.UserRepository
import com.example.spectrplus.service.education.FileStorageService
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class ProfileService(

    private val userRepository: UserRepository,
    private val childRepository: ChildRepository,
    private val therapyRepository: TherapyRepository,
    private val fileStorage: FileStorageService

) {

    fun getProfile(email: String): ProfileResponse {

        println("EMAIL FROM TOKEN: $email")
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val children = childRepository.findByUserId(user.id)

        val childResponses = children.map { child ->

            val therapies = therapyRepository.findByChildId(child.id)

            ChildResponse(
                id = child.id,
                name = child.name,
                age = child.age,
                gender = child.gender,
                diagnosis = child.diagnosis,
                features = child.features,
                notes = child.notes,
                therapies = therapies.map {
                    TherapyResponse(it.title, it.description)
                }
            )
        }

        return ProfileResponse(
            id = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            phone = user.phone,
            city = user.city,
            accountRole = user.accountRole.name,
            showChildInPublicProfile = user.showChildInPublicProfile,
            specialistProfession = user.specialistProfession,
            specialistEducation = user.specialistEducation,
            specialistExperienceYears = user.specialistExperienceYears,
            avatarUrl = user.avatarUrl,
            children = childResponses
        )
    }

    fun updateProfile(email: String, request: UpdateProfileRequest) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        user.firstName = request.firstName
        user.lastName = request.lastName
        user.phone = request.phone
        user.city = request.city
        user.showChildInPublicProfile = request.showChildInPublicProfile
        user.specialistProfession = request.specialistProfession
        user.specialistEducation = request.specialistEducation
        user.specialistExperienceYears = request.specialistExperienceYears

        userRepository.save(user)
    }

    fun uploadAvatar(email: String, file: MultipartFile): String {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val original = file.originalFilename ?: "avatar.jpg"
        val bytes = file.bytes
        val url = fileStorage.store("avatars", original, bytes, file.contentType)
        user.avatarUrl = url
        userRepository.save(user)

        return url
    }

    fun deleteAvatar(email: String) {
        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")
        user.avatarUrl = null
        userRepository.save(user)
    }
}