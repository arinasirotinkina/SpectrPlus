package com.example.spectrplus.data.dto.articles

data class MaterialDto(
    val id: Long,
    val title: String,
    val description: String,
    val fileUrl: String,
    val type: String,
    val category: String,
    val fileSize: Long = 0L,
    val sourceAttribution: String? = null
)