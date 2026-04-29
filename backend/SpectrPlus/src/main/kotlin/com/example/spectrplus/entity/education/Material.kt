package com.example.spectrplus.entity.education

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "materials")
data class Material(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    val description: String,

    val fileUrl: String,

    @Enumerated(EnumType.STRING)
    val type: MaterialType,

    @Enumerated(EnumType.STRING)
    val category: MaterialCategory,

    val fileSize: Long,

    val sourceAttribution: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now()
)