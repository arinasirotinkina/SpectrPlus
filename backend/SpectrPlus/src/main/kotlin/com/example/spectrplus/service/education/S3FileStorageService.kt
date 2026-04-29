package com.example.spectrplus.service.education

import com.example.spectrplus.config.StorageProperties
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.S3Configuration
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.net.URI
import java.util.UUID

class S3FileStorageService(
    private val properties: StorageProperties
) : FileStorageService {

    private val client: S3Client by lazy {
        val creds = AwsBasicCredentials.create(properties.s3AccessKey, properties.s3SecretKey)
        S3Client.builder()
            .endpointOverride(URI.create(properties.s3Endpoint))
            .region(Region.of(properties.s3Region))
            .credentialsProvider(StaticCredentialsProvider.create(creds))
            .serviceConfiguration(
                S3Configuration.builder()
                    .pathStyleAccessEnabled(true)
                    .build()
            )
            .build()
    }

    override fun store(prefix: String, originalFilename: String, bytes: ByteArray, contentType: String?): String {
        val ext = originalFilename.substringAfterLast('.', "bin").take(12)
        val key = "$prefix/${UUID.randomUUID()}.$ext"
        val put = PutObjectRequest.builder()
            .bucket(properties.s3Bucket)
            .key(key)
            .contentType(contentType ?: "application/octet-stream")
            .build()
        client.putObject(put, RequestBody.fromBytes(bytes))
        val base = properties.s3PublicBaseUrl.trimEnd('/')
        return "$base/$key"
    }
}
