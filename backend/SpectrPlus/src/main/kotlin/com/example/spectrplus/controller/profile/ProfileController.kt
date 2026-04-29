package com.example.spectrplus.controller.profile

import com.example.spectrplus.dto.profile.ProfileResponse
import com.example.spectrplus.dto.profile.UpdateProfileRequest
import com.example.spectrplus.service.profile.ProfileService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/profile")
class ProfileController(
    private val profileService: ProfileService
) {

    @GetMapping
    fun getProfile(
        authentication: Authentication
    ): ProfileResponse {

        val email = authentication.name

        return profileService.getProfile(email)
    }

    @PutMapping
    fun updateProfile(
        authentication: Authentication,
        @RequestBody request: UpdateProfileRequest
    ) {

        val email = authentication.name

        profileService.updateProfile(email, request)
    }

    @PostMapping(
        "/avatar",
        )
    fun uploadAvatar(
        authentication: Authentication,
        @RequestPart("file") file: MultipartFile
    ): Map<String, String> {
        val url = profileService.uploadAvatar(authentication.name, file)
        return mapOf("avatarUrl" to url)
    }

    @DeleteMapping("/avatar")
    fun deleteAvatar(authentication: Authentication) {
        profileService.deleteAvatar(authentication.name)
    }
}