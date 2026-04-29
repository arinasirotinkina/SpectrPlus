package com.example.spectrplus.config

import com.example.spectrplus.service.education.FileStorageService
import com.example.spectrplus.service.education.LocalFileStorageService
import com.example.spectrplus.service.education.S3FileStorageService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig {

    @Bean
    fun fileStorageService(properties: StorageProperties): FileStorageService {
        val s3Ready = properties.s3Bucket.isNotBlank() &&
            properties.s3AccessKey.isNotBlank() &&
            properties.s3SecretKey.isNotBlank() &&
            properties.s3PublicBaseUrl.isNotBlank()
        return if (s3Ready) S3FileStorageService(properties) else LocalFileStorageService()
    }
}
