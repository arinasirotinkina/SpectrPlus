package com.example.spectrplus.entity.social

import com.example.spectrplus.entity.social.ForumCategory
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "topics")
data class TopicEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    @Enumerated(EnumType.STRING)
    val category: ForumCategory,

    val authorId: Long,

    val authorName: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)