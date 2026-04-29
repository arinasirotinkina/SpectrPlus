package com.example.spectrplus.data.repository.social

import com.example.spectrplus.core.network.absoluteUrl
import com.example.spectrplus.data.api.profile.UserApi
import com.example.spectrplus.domain.model.profile.Therapy
import com.example.spectrplus.domain.model.social.PublicChildProfile
import com.example.spectrplus.domain.model.social.PublicUser
import com.example.spectrplus.domain.repository.social.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserApi
) : UserRepository {

    override suspend fun getPublicUser(id: Long): PublicUser {
        val dto = api.getById(id)
        val child = dto.publicChildProfile?.let { c ->
            PublicChildProfile(
                name = c.name,
                age = c.age,
                gender = c.gender,
                diagnosis = c.diagnosis,
                therapies = c.therapies.map { Therapy(it.title, it.description) }
            )
        }
        return PublicUser(
            id = dto.id,
            firstName = dto.firstName,
            lastName = dto.lastName,
            avatarUrl = absoluteUrl(dto.avatarUrl),
            city = dto.city,
            accountRole = dto.accountRole,
            specialistProfession = dto.specialistProfession,
            specialistEducation = dto.specialistEducation,
            specialistExperienceYears = dto.specialistExperienceYears,
            topicsCount = dto.topicsCount,
            postsCount = dto.postsCount,
            showChildInPublicProfile = dto.showChildInPublicProfile,
            publicChildProfile = child
        )
    }
}
