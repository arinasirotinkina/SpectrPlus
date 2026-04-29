package com.example.spectrplus.entity.education

import jakarta.persistence.*

@Entity
@Table(name = "articles")
class Article(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val title: String,

    @Column(columnDefinition = "TEXT")
    val content: String,

    val author: String,

    @Enumerated(EnumType.STRING)
    val category: ArticleCategory,

    val sourceAttribution: String? = null,

    val coverImageUrl: String? = null
)