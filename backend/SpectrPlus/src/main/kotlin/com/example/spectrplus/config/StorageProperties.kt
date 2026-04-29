package com.example.spectrplus.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "spectr.storage")
data class StorageProperties(
    var s3Bucket: String = "",
    var s3Endpoint: String = "https://s3.ru1.storage.beget.cloud",
    var s3Region: String = "ru-1",
    var s3AccessKey: String = "",
    var s3SecretKey: String = "",
    var s3PublicBaseUrl: String = ""
)
