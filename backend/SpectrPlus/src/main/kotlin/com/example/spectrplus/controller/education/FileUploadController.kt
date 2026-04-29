package com.example.spectrplus.controller.education

import com.example.spectrplus.service.education.FileStorageService
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/files")
class FileUploadController(
    private val fileStorage: FileStorageService
) {

    @PostMapping("/upload")
    fun upload(
        authentication: Authentication,
        @RequestPart("file") file: MultipartFile,
        @RequestParam(defaultValue = "uploads") folder: String
    ): Map<String, String> {
        val safeFolder = folder.replace(Regex("[^a-zA-Z0-9_-]"), "").ifBlank { "uploads" }
        val original = file.originalFilename ?: "file.bin"
        val url = fileStorage.store(safeFolder, original, file.bytes, file.contentType)
        return mapOf("url" to url)
    }
}