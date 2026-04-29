package com.example.spectrplus.dto.education.material

data class MaterialResponse(
    val id: Long,
    val title: String,
    val description: String,
    val fileUrl: String,
    val type: String,
    val category: String,
    val fileSize: Long,
    val sourceAttribution: String? = null
)