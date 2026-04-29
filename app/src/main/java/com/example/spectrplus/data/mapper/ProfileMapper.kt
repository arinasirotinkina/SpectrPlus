package com.example.spectrplus.data.mapper

import com.example.spectrplus.core.network.absoluteUrl
import com.example.spectrplus.data.dto.profile.ChildDto
import com.example.spectrplus.data.dto.profile.ProfileResponseDto
import com.example.spectrplus.data.dto.profile.UpdateProfileRequestDto
import com.example.spectrplus.domain.model.profile.Child
import com.example.spectrplus.domain.model.profile.Profile
import com.example.spectrplus.domain.model.profile.Therapy
import com.example.spectrplus.domain.model.profile.UpdateProfileRequest

fun ProfileResponseDto.toDomain(): Profile {
    return Profile(
        id = id,
        firstName = firstName,
        lastName = lastName,
        email = email,
        phone = phone,
        city = city,
        avatarUrl = absoluteUrl(avatarUrl),
        accountRole = accountRole,
        showChildInPublicProfile = showChildInPublicProfile,
        specialistProfession = specialistProfession,
        specialistEducation = specialistEducation,
        specialistExperienceYears = specialistExperienceYears,
        children = children.map { it.toDomain() }
    )
}

fun ChildDto.toDomain(): Child {
    return Child(
        id = id,
        name = name,
        age = age,
        gender = gender,
        diagnosis = diagnosis,
        features = features,
        notes = notes,
        therapies = therapies.map { Therapy(it.title, it.description) }
    )
}

fun UpdateProfileRequest.toDto(): UpdateProfileRequestDto {
    return UpdateProfileRequestDto(
        firstName = firstName,
        lastName = lastName,
        phone = phone,
        city = city,
        showChildInPublicProfile = showChildInPublicProfile,
        specialistProfession = specialistProfession,
        specialistEducation = specialistEducation,
        specialistExperienceYears = specialistExperienceYears
    )
}
