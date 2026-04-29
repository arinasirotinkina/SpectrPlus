package com.example.spectrplus.entity.education

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "videos")
data class Video(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    val description: String,

    val videoUrl: String,

    val thumbnailUrl: String,

    val durationSeconds: Int,

    @Enumerated(EnumType.STRING)
    val category: VideoCategory,

    val subtitlesUrl: String? = null,

    val sourceAttribution: String? = null,

    val createdAt: LocalDateTime = LocalDateTime.now()
)