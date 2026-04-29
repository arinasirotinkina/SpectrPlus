package com.example.spectrplus.service.education

import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

class LocalFileStorageService : FileStorageService {

    override fun store(prefix: String, originalFilename: String, bytes: ByteArray, contentType: String?): String {
        val ext = originalFilename.substringAfterLast('.', "bin").take(8)
        val filename = "${UUID.randomUUID()}.$ext"
        val dir = Paths.get("uploads", prefix)
        Files.createDirectories(dir)
        val target = dir.resolve(filename)
        Files.write(target, bytes)
        return "/uploads/$prefix/$filename"
    }
}
