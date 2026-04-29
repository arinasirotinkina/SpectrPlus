package com.example.spectrplus.entity.social

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "posts")
data class PostEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val topicId: Long,

    val authorId: Long,

    val authorName: String,

    val authorProfessionalLabel: String? = null,

    @Column(columnDefinition = "TEXT")
    var content: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
