package com.example.spectrplus.core.network

const val BASE_URL = "http://10.0.2.2:8080"

fun absoluteUrl(path: String?): String? {
    if (path.isNullOrBlank()) return null
    if (path.startsWith("http://") || path.startsWith("https://")) return path
    return if (path.startsWith("/")) "$BASE_URL$path" else "$BASE_URL/$path"
}
