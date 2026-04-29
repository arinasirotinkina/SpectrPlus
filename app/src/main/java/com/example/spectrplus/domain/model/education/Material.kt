package com.example.spectrplus.domain.model.education

data class Material(
    val id: Long,
    val title: String,
    val description: String,
    val fileUrl: String,
    val type: String,
    val category: String
)