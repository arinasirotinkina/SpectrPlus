package com.example.spectrplus.entity.education

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "article_comments")
data class ArticleComment(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val articleId: Long,

    val authorId: Long,

    val authorName: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)
