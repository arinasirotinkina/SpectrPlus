package com.example.spectrplus.service.education

interface FileStorageService {

    fun store(prefix: String, originalFilename: String, bytes: ByteArray, contentType: String?): String
}
