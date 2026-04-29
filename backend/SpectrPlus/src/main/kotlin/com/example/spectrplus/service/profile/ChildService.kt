package com.example.spectrplus.service.profile

import com.example.spectrplus.dto.profile.*
import com.example.spectrplus.dto.profile.child.CreateChildRequest
import com.example.spectrplus.entity.profile.Child
import com.example.spectrplus.entity.profile.Therapy
import com.example.spectrplus.repository.profile.ChildRepository
import com.example.spectrplus.repository.profile.TherapyRepository
import com.example.spectrplus.repository.profile.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChildService(
    private val userRepository: UserRepository,
    private val childRepository: ChildRepository,
    private val therapyRepository: TherapyRepository
) {

    @Transactional
    fun addChild(email: String, request: CreateChildRequest) {

        val user = userRepository.findByEmail(email)
            ?: throw RuntimeException("User not found")

        val child = childRepository.save(
            Child(
                userId = user.id,
                name = request.name,
                age = request.age,
                gender = request.gender,
                diagnosis = request.diagnosis,
                features = request.features,
                notes = request.notes
            )
        )

        if (request.therapies.isNotEmpty()) {
            therapyRepository.saveAll(
                request.therapies.map {
                    Therapy(
                        childId = child.id,
                        title = it.title,
                        description = it.description
                    )
                }
            )
        }
    }

    @Transactional
    fun updateChild(childId: Long, request: UpdateChildRequest) {

        val child = childRepository.findById(childId)
            .orElseThrow { RuntimeException("Child not found") }

        child.name = request.name
        child.age = request.age
        child.gender = request.gender
        child.diagnosis = request.diagnosis
        child.features = request.features
        child.notes = request.notes

        childRepository.save(child)

        therapyRepository.deleteByChildId(childId)

        if (request.therapies.isNotEmpty()) {
            therapyRepository.saveAll(
                request.therapies.map {
                    Therapy(
                        childId = childId,
                        title = it.title,
                        description = it.description
                    )
                }
            )
        }
    }

    @Transactional
    fun deleteChild(childId: Long) {
        therapyRepository.deleteByChildId(childId)
        childRepository.deleteById(childId)
    }
}
